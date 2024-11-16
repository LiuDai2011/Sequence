import binascii

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

    def __init__(self, length: int, name: str, data: bytes, crc: int | None = None):
        if length == -1:
            length = len(data)
        self.length = length
        self.name = name
        self.data = data
        if crc is None:
            crc = crc32(bytes(name, encoding='ascii') + data)
        self.crc = crc

    @staticmethod
    def read_block(dat: bytes, file):
        length = toint(dat[:4])
        dat = dat[4:]
        print(f"BLOCK LENGTH: {length}", file=file)

        name = tostr(dat[:4])
        dat = dat[4:]
        print(f"BLOCK NAME: {name}", file=file)

        da_bytes = dat[:length]
        da = ' '.join(tod(da_bytes))
        dat = dat[length:]
        print(f"BLOCK DATA: {da}", file=file)

        crc = ' '.join(tod(dat[:4]))
        crc_int = toint(dat[:4])
        dat = dat[4:]
        print(f"BLOCK CRC: {crc}", file=file)

        Block.blocks.append(Block(length, name, da_bytes, crc_int))

        print(file=file)
        return dat

    def __str__(self):
        return f"{{{self.length}, {self.name}, {' '.join(tod(self.data))}, {self.crc}}}"


if __name__ == '__main__':
    with open("../origin/icon-origin.png", "rb") as origin:
        data = origin.read()
    with open("./origin-hex.txt", "w") as f:
        t = tod(data)
        c = 1
        for e in t:
            f.write(e + '  ')
            if c % 16 == 0:
                f.write('\n')
            c += 1
    with open("./origin-block.txt", "w") as f:
        data = data[8:]
        while len(data):
            data = Block.read_block(data, f)
    Block.blocks.insert(2, Block(-1, "tExt", bytes("[#ceeaf4]Liu Dai[]", encoding='ascii')))
    a = open("./origin-added.txt", "w")
    print([str(i) for i in Block.blocks], file=a)
    a.close()
