import binascii
import hashlib
import os
import sys

crc32 = lambda x: binascii.crc32(x) & 0xffffffff
tod = lambda dat: [(lambda x: x if len(x) > 1 else '0' + x)(str(hex(i))[2:]).upper() for i in dat]


def toint(byt: bytes):
    r = 0
    for i in byt:
        r = r * 256 + i
    return r


def tostr(byt: bytes):
    r = ''
    for i in byt:
        r += chr(i)
    return r


class Block:
    blocks = []
    header = 0x89504E470D0A1A0A.to_bytes(8, "big")

    def __init__(self, length: int, name: str, data: bytes, crc: int | None = None):
        if length == -1:
            length = len(data)
        self.length = length
        self.name = name
        self.data = data
        if crc is None:
            crc = crc32(bytes(name, encoding='ascii') + data)
        self.crc = crc

    def __bytes__(self):
        return self.length.to_bytes(4, "big") + bytes(self.name, encoding='ascii') + \
            self.data + self.crc.to_bytes(4, "big")

    @staticmethod
    def read_block(dat: bytes, log):
        length = toint(dat[:4])
        dat = dat[4:]
        print(f"BLOCK LENGTH: {length}", file=log)

        name = tostr(dat[:4])
        dat = dat[4:]
        print(f"BLOCK NAME: {name}", file=log)

        da_bytes = dat[:length]
        da = ' '.join(tod(da_bytes))
        dat = dat[length:]
        print(f"BLOCK DATA: {da}", file=log)

        crc = ' '.join(tod(dat[:4]))
        crc_int = toint(dat[:4])
        dat = dat[4:]
        print(f"BLOCK CRC: {crc}", file=log)

        Block.blocks.append(Block(length, name, da_bytes, crc_int))

        print(file=log)
        return dat

    @staticmethod
    def reads(dat: bytes, log):
        Block.blocks.clear()
        dat = dat[8:]
        while len(dat):
            dat = Block.read_block(dat, log)
        return Block.blocks

    @staticmethod
    def writes(png):
        png.write(Block.header + b''.join([bytes(i) for i in Block.blocks]))

    def __str__(self):
        return f"{{{self.length}, {self.name}, {' '.join(tod(self.data))}, {self.crc}}}"

    def __repr__(self):
        return self.__str__()


if __name__ == '__main__':
    with open("../origin/icon-origin.png", "rb") as origin:
        data = origin.read()
    Block.reads(data, sys.stdout)
    HASH = 0
    hasher = hashlib.sha3_512()
    path = os.path.join("..", "assets")
    for dirname, subdir, files in os.walk(path):
        for f in files:
            if f == 'icon.png':
                continue
            abf = os.path.join(dirname, f).replace('\\', '/')
            with open(abf, "rb") as fi:
                d = fi.read()
            abf = abf[abf.find('/') + 1:]
            abf = abf[abf.find('/') + 1:]
            print(abf)
            hasher.update(bytes(abf, encoding='ascii') + d)
            HASH = ((HASH << 16) ^ int(hasher.hexdigest(), base=16)) & 0xffff_ffff_ffff_ffff
    print(HASH)

    Block.blocks.insert(2, Block(-1, "tExt", bytes("[#ceeaf4]Liu Dai[]", encoding='ascii')))
    Block.blocks.insert(2, Block(-1, "tExt", bytes("Author: Ned-Kelly", encoding='ascii')))
    Block.blocks.insert(2, Block(-1, "zTxt", HASH.to_bytes(8, "big")))

    with open("./origin-added.png", "wb") as f:
        Block.writes(f)
