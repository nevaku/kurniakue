var msgvm;

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
            msgs: [],
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
            addMsg(msgText, isActive) {
                //console.log(msgText + ", " + isActive);
                newLength = this.msgs.push({
                    id: this.nextMsgId++,
                    title: msgText
                });
                if (isActive) {
                    this.activate(newLength - 1);
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