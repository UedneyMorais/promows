/**
 * Dropdowns acessíveis: botão + painel, um aberto por vez, clique fora fecha, Escape fecha.
 * Após inserir o HTML do menu, chame initNavDropdowns(container).
 */
(function (global) {
    var docListenersBound = false;

    function closeAllDropdowns() {
        var drops = document.querySelectorAll('[data-dropdown]');
        for (var i = 0; i < drops.length; i++) {
            var drop = drops[i];
            var btn = drop.querySelector('.nav-dropdown__trigger');
            var panel = drop.querySelector('.nav-dropdown__panel');
            drop.classList.remove('is-open');
            if (btn) btn.setAttribute('aria-expanded', 'false');
            if (panel) panel.hidden = true;
        }
    }

    function bindDocumentOnce() {
        if (docListenersBound) return;
        docListenersBound = true;
        document.addEventListener('click', function () {
            closeAllDropdowns();
        });
        document.addEventListener('keydown', function (e) {
            if (e.key === 'Escape') closeAllDropdowns();
        });
    }

    function initNavDropdowns(root) {
        var scope = root || document;
        var dropdowns = scope.querySelectorAll('[data-dropdown]:not([data-dropdown-inited])');

        bindDocumentOnce();

        for (var d = 0; d < dropdowns.length; d++) {
            (function (drop) {
                drop.setAttribute('data-dropdown-inited', 'true');
                var btn = drop.querySelector('.nav-dropdown__trigger');
                var panel = drop.querySelector('.nav-dropdown__panel');
                if (!btn || !panel) return;

                btn.addEventListener('click', function (e) {
                    e.stopPropagation();
                    var isOpen = btn.getAttribute('aria-expanded') === 'true';
                    closeAllDropdowns();
                    if (!isOpen) {
                        btn.setAttribute('aria-expanded', 'true');
                        panel.hidden = false;
                        drop.classList.add('is-open');
                    }
                });

                drop.addEventListener('click', function (e) {
                    e.stopPropagation();
                });
            })(dropdowns[d]);
        }
    }

    global.initNavDropdowns = initNavDropdowns;
    global.closeAllNavDropdowns = closeAllDropdowns;
})(typeof window !== 'undefined' ? window : this);
