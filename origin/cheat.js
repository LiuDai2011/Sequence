Vars.content.statusEffect("seq-broken-shield").damage = 100;
Vars.content.block("seq-ttt").ammoTypes.get(Vars.content.item("seq-encapsulated-imagine-energy")).lifetime = 100;

Vars.content.each(cons(it => {
    if (it instanceof UnlockableContent) it.unlock();
}));
Vars.state.rules.infiniteResources = true;