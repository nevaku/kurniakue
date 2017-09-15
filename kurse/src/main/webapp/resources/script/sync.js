var executor =
        {
            running: false,
            waiter: null,
            currentIndex: 0,
            functionList: [],
            waitCounter: 0,
            clear: function ()
            {
                this.functionList = [];
            },
            _do: function (f)
            {
                f.mark = "do";
                this.functionList.push(f);
                return this.functionList.length;
            },
            _stop: function (f)
            {
                f.mark = "stop";
                this.functionList.push(f);
                return this.functionList.length;
            },
            _if: function (f)
            {
                f.mark = "if";
                this.functionList.push(f);
                return this.functionList.length;
            },
            _elseif: function (f)
            {
                f.mark = "elseif";
                this.functionList.push(f);
                return this.functionList.length;
            },
            _else: function (f)
            {
                f.mark = "else";
                this.functionList.push(f);
                return this.functionList.length;
            },
            _endif: function (f)
            {
                f.mark = "endif";
                this.functionList.push(f);
                return this.functionList.length;
            },
            _wait: function (f)
            {
                f.mark = "wait";
                this.functionList.push(f);
                return this.functionList.length;
            },
            start: function ()
            {
                console.info("Starting");
                this.running = true;
                this.currentIndex = 0;
                this.waiter = setTimeout(this.run, 0);
                return this.functionList.length;
            },
            continue: function ()
            {
                console.info("Continue at " + this.currentIndex);
                this.running = true;
                this.waiter = setTimeout(this.run, 0);
                return this.currentIndex;
            },
            run: function (the)
            {
                the = executor;
                if (!the.running)
                {
                    console.info("Stop signal received, return immediately");
                    return;
                }

                if (the.currentIndex < 0)
                {
                    console.info("Invalid index: " + the.currentIndex);
                    return;
                }
                if (the.currentIndex >= the.functionList.length)
                {
                    console.info("Invalid index: " + the.currentIndex);
                    console.info("Max: " + the.functionList.length - 1);
                    return;
                }

                currentFunction = the.functionList[the.currentIndex];
                console.info(the.currentIndex + " - " + currentFunction.mark);
                
                if (currentFunction.mark === "wait")
                {
                    result = currentFunction.call();
                    console.info(the.waitCounter + " - Wait result: " + result);
                    if (result)
                    {
                        the.waitCounter = 0;
                        the.currentIndex += 1;
                    }
                    the.waitCounter += 1;
                } else if (currentFunction.mark === "if" || currentFunction.mark === "elseif")
                {
                    result = currentFunction.call();
                    if (result)
                    {
                        the.currentIndex += 1;
                    } else
                    {
                        the.findNextIf();
                    }
                } else
                {
                    currentFunction.call();
                    the.currentIndex += 1;
                    if (currentFunction.mark === "stop")
                    {
                        the.running = false;
                        console.info("Call continue() to run next execution");
                        return;
                    }
                }

                if (the.currentIndex >= the.functionList.length)
                {
                    return;
                }
                // Progresif wait, first 100ms, then 200ms, 300ms, ...
                this.waiter = setTimeout(the.run, 100 * (the.waitCounter));
            },
            findNextIf: function ()
            {
                currentFunction = this.functionList[this.currentIndex];
                stackCount = 0;
                while (this.currentIndex < this.functionList.length)
                {
                    if (currentFunction.mark === "elseif")
                    {
                        if (stackCount === 0)
                        {
                            return;
                        }
                    } else if (currentFunction.mark === "else")
                    {
                        if (stackCount === 0)
                        {
                            this.currentIndex += 1;
                            return;
                        }
                    } else if (currentFunction.mark === "endif")
                    {
                        if (stackCount === 0)
                        {
                            this.currentIndex += 1;
                            return;
                        } else
                        {
                            if (stackCount > 0)
                            {
                                stackCount -= 1;
                            }
                        }
                    } else if (currentFunction.mark === "if")
                    {
                        stackCount += 1;
                    }

                    this.currentIndex += 1;
                }
            }
        };

function _do(f)
{
    executor._do(f);
}

function _wait(f)
{
    executor._wait(f);
}

function _stop(f)
{
    executor._stop(f);
}

var counter = 1;

// sample usage
// do something, that wait for a condition to be true.
// in the real word, this can be used to wait for a component 
// exists after click an ajax call update, such as
// return $("#newTextBox").size()
function wait_for_condition()
{
    // clear previous execution stacks
    executor.clear();

    // start registering execution blocks
    _do(() => {
        console.info("First");
        console.info("Another First");
    });
    _do(() => {
        console.info("Second");
        console.info("Another Second");
    });
    _wait(() => {
        console.info("Wait for 5 counter: " + counter);
        return (++counter > 5);
    });
    _do(() => {
        console.info("Third");
        counter = 1;
        console.info("Reset the counter to " + counter);
    });
    _wait(() => {
        console.info("Wait for 15 counter: " + counter);
        return (++counter > 15);
    });
    _do(() => {
        console.info("Done");
    });
    return executor.start();
}

// Calling an ajax, and wait for it returns before continue.
// The wait statements will be executed repeatedly until it return true,
// before the executor move to next execution block.
// in order this to work, please create a file ajaxSample.html
// or change the url to match any file in your environtment
function wait_for_ajax_pooling()
{
    // clear previous execution stacks
    executor.clear();

    // prepare ajax variable
    var xhttp;

    // call ajax
    _do(() => {
        console.info("get data from ajaxSample.html");
        xhttp = new XMLHttpRequest();
        xhttp.open("GET", "ajaxSample.html");
        xhttp.send();
    });

    // wait for ajax response ready
    _wait(() => {
        console.info("Wait for ajax status ready: " + xhttp.readyState);
        return (xhttp.readyState === 4);
    });

    _do(() => {
        console.info("Ajax Status: " + xhttp.status);
        console.info("Ajax Response: " + xhttp.responseText);
    });

    return executor.start();
}

// Calling an ajax, and wait for it returns before continue.
// The difference between previous example is that the execution
// will stop after ajax is called. 
// It waits for continue() to run the next block.
// executor.continue() is called when "onreadystatechange"
// in order this to work, please create a file ajaxSample.html
// or change the url to match any file in your environtment
function wait_for_ajax_stop_continue()
{
    // clear previous execution stacks
    executor.clear();

    // prepare ajax variable
    var xhttp;

    // call ajax, and stop
    // notified by onreadystatechange, then continue
    // no need to wait
    _stop(() => {
        console.info("get status from server");
        xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState === 4) {
                executor.continue();
            }
        };
        xhttp.open("GET", "ajaxSample.html");
        xhttp.send();
    });

    _do(() => {
        console.info("Ajax Status: " + xhttp.status);
        console.info("Ajax Response: " + xhttp.responseText);
    });

    return executor.start();
}

