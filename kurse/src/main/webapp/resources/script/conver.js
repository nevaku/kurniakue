$(function () {
    Vue.component('msg-item', {
        template: '\
            <a href="#" v-on:click="$emit(\'activate\')">\
              {{ title }}\
              <span class="badge"><button v-on:click="$emit(\'remove\')">X</button></span>\
            </a>\
          ',
        props: ['title']
    });

    msgvm = new Vue({
        el: '#msg-list',
        data: {
            newMsgText: '',
            msgs: [
                {
                    id: 1,
                    title: 'Do the dishes',
                    isActive: true
                },
                {
                    id: 2,
                    title: 'Take out the trash',
                    isActive: false
                },
                {
                    id: 3,
                    title: 'Mow the lawn',
                    isActive: false
                }
            ],
            nextMsgId: 4,
            activeIndex: 0
        },
        methods: {
            addNewMsg: function () {
                this.msgs.push({
                    id: this.nextMsgId++,
                    title: this.newMsgText
                });
                this.newMsgText = '';
            },
            remove(ixRemove, opt) {
                this.msgs.splice(ixRemove, opt);
                if (this.activeIndex === ixRemove) {
                    this.activeIndex = -1;
                } else if (this.activeIndex > ixRemove) {
                    this.activeIndex -= 1;
                }
            },
            activate(ixActivate) {
                if (ixActivate === this.activeIndex) {
                    return;
                }
                if (this.activeIndex >= 0 && this.activeIndex < this.msgs.length) {
                    this.msgs[this.activeIndex].isActive = false;
                }
                this.msgs[ixActivate].isActive = true;
                this.activeIndex = ixActivate;
            }
        }
    });
});