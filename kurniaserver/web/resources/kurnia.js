$(function () {
    Vue.component('kk-button', {
        template: 
                '<div class="col-xs-4 col-sm-3">\
                        <button v-on:click="kkopen" class="btn-block">{{text}}</button>\
                </div>',
        props: ['text','href'],
        methods: {
            kkopen: function () {
                vm.openhref(this.href);
            }
        }
    });

    vm =new Vue({
        el: '#kkwadah',
        methods: {
            openhref: function (href) {
                document.location.href = href;
            },
            openula: function () {
                document.location.href = "ula.html";
            },
            calculate: function () {
                // submitting;
                $('#caldlg').modal('hide');
            }
        }
    });
});