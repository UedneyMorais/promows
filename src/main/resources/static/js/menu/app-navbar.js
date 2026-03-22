(function () {
    var mount = document.getElementById('app-navbar-mount');
    if (!mount) return;

    function afterNavInjected(navRoot) {
        var active = document.body.getAttribute('data-nav-active');
        if (active) {
            var els = document.querySelectorAll('[data-nav="' + active + '"]');
            for (var i = 0; i < els.length; i++) {
                els[i].classList.add('active');
            }
        }
        if (typeof window.initNavDropdowns === 'function') {
            window.initNavDropdowns(navRoot);
        }
    }

    fetch('/partials/app-navbar.html', { cache: 'no-cache' })
        .then(function (r) {
            if (!r.ok) throw new Error('nav');
            return r.text();
        })
        .then(function (html) {
            var temp = document.createElement('div');
            temp.innerHTML = html.trim();
            var nav = temp.firstElementChild;
            if (!nav || !mount.parentNode) return;
            mount.parentNode.replaceChild(nav, mount);
            afterNavInjected(nav);
        })
        .catch(function () {
            mount.innerHTML =
                '<p class="nav-load-error" style="padding:1rem;color:#b91c1c;">Não foi possível carregar o menu. Atualize a página.</p>';
        });
})();
