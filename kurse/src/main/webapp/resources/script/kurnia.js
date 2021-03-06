var statusRefresh = {
    reqid: 0,
    bean: "Status",
    method: "refresh"
};

var lastid = 0;

var officer = {
    started: true,
    accept: function (reqid, bean, method, params) {
        if (typeof (this.waiter) !== "undefined") {
            clearTimeout(this.waiter);
        }

        if (typeof (reqid) === "undefined")
        {
            reqid = lastid + 1;
            lastid = reqid;
        }
        jreq = {
            reqid: reqid,
            bean: bean,
            method: method,
            params: params
        };
        poster.send(jreq);
        return "OK";
    },
    check: function () {
        poster.send(statusRefresh);
        return "OK";
    },
    stop: function() {
        started = false;
        if (typeof (this.waiter) !== "undefined") {
            clearTimeout(this.waiter);
        }
        return "OK";
    }
};

var poster = {
    send: function (msg) {
        req = new XMLHttpRequest();
        req.poster = poster;
        req.reqid = msg.reqid;
        req.open("POST", "kursepo", true);
        req.setRequestHeader("Content-type", "application/json");
        req.onreadystatechange = function () {
            if (this.readyState === 1) {
                console.log("connected: " + this.reqid);
            } else if (this.readyState === 2) {
                console.log("request sent: " + this.reqid);
            } else if (this.readyState === 4) {
                console.log("reply received: " + this.reqid);
                if (this.status === 200 && this.responseText.trim() !== "NA") {
                    this.poster.receive(this);
                }
                
                if (officer.started) {
                    officer.waiter = setTimeout("officer.check()", 5000);
                }
            }
        };
        data = JSON.stringify(msg);
        req.send(data);
        return req;
    },
    receive: function (req) {
        console.log("Processing reply: " + req.reqid);
        console.log(req.responseText);
        msgvm.addMsg(req.responseText);
        return "OK";
    }
};

var tip = {
    request: function () {
        msgvm.msgs.push({id: msgvm.nextMsgId++, title: "from outer " + msgvm.nextMsgId});
    }
};

$(function () {
    //officer.check();
    
    Vue.component('kk-button', {
        template:
                '<div class="col-xs-4 col-sm-3">\
                        <button v-on:click="kkopen" class="btn-block">{{text}}</button>\
                </div>',
        props: ['text', 'href'],
        methods: {
            kkopen: function () {
                vm.openhref(this.href);
            }
        }
    });
    
    Vue.component('kk-command', {
        template:
                '<div class="col-xs-4 col-sm-3">\
                    <button class="btn-block" >{{text}}</button>\
                </div>',
        props: ['text']
    });

    vm = new Vue({
        el: '#kkwadah',
        methods: {
            openhref: function (href) {
                document.location.href = href;
            },
            openula: function () {
                document.location.href = "ula.html";
            },
            calculate: function () {
                rkp.calculate();
                $('#caldlg').modal('hide');
            }
        }
    });
});