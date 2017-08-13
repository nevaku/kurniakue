var rkp = {};

$(function () {
    rkp.calculate = function () {
        req = new XMLHttpRequest();
        req.open("POST", "kursepo", true);
        req.setRequestHeader("Content-type", "application/json");
        req.onreadystatechange = function () {
            if (req.readyState === 4 && req.status === 200) {
                console.log(req.responseText);
            }
        };
        
        jreq = {};
        jreq.bean = "rekap";
        jreq.method = "calculate";
        var data = JSON.stringify(jreq);
        req.send(data);
    };
});