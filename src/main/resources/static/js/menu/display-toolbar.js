(function () {
    var mount = document.getElementById('display-app-nav-mount');
    if (!mount) return;

    fetch('/partials/display-toolbar.html', { cache: 'no-cache' })
        .then(function (r) {
            if (!r.ok) throw new Error('toolbar');
            return r.text();
        })
        .then(function (html) {
            var temp = document.createElement('div');
            temp.innerHTML = html.trim();
            var el = temp.firstElementChild;
            if (!el || !mount.parentNode) return;
            mount.parentNode.replaceChild(el, mount);
            if (typeof window.initNavDropdowns === 'function') {
                window.initNavDropdowns(el);
            }
        })
        .catch(function () {
            mount.innerHTML = '';
        });
})();
