var executor =
        {
            running: false,
            waiter: null,
            currentIndex: 0,
            functionList: [],
            waitCounter: 0,
            silentMode: 0,
            consoleInfo: function (text)
            {
                if (this.silentMode)
                {
                    return;
                }
                console.info("[Executor] " + text);
            },
            silent()
            {
                this.silentMode = 1;
            },
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
            _else: function ()
            {
                f = function () {};
                f.mark = "else";
                this.functionList.push(f);
                return this.functionList.length;
            },
            _endif: function ()
            {
                f = function () {};
                f.mark = "endif";
                this.functionList.push(f);
                return this.functionList.length;
            },
            _while: function (f)
            {
                f.mark = "while";
                this.functionList.push(f);
                return this.functionList.length;
            },
            _endwhile: function ()
            {
                f = function () {};
                f.mark = "endwhile";
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
                this.consoleInfo("Starting");
                this.running = true;
                this.currentIndex = 0;
                this.waiter = setTimeout(this.run, 0);
                return this.functionList.length;
            },
            continue: function ()
            {
                this.consoleInfo("Continue at " + this.currentIndex);
                this.running = true;
                this.waiter = setTimeout(this.run, 0);
                return this.currentIndex;
            },
            run: function (the)
            {
                the = executor;
                if (!the.running)
                {
                    the.consoleInfo("Stop signal received, return immediately");
                    return;
                }

                if (the.currentIndex < 0)
                {
                    the.consoleInfo("Invalid index: " + the.currentIndex);
                    return;
                }
                if (the.currentIndex >= the.functionList.length)
                {
                    the.consoleInfo("Invalid index: " + the.currentIndex);
                    the.consoleInfo("Max: " + the.functionList.length - 1);
                    return;
                }

                currentFunction = the.functionList[the.currentIndex];
                the.consoleInfo(the.currentIndex + " - " + currentFunction.mark);

                if (currentFunction.mark === "wait")
                {
                    result = currentFunction.call();
                    the.consoleInfo(the.waitCounter + " - Wait result: " + result);
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
                        the.currentIndex += 1;
                        the.gotoNextIf();
                    }
                } else if (currentFunction.mark === "else")
                {
                    // found else after executing if
                    // skip through to endif
                    the.currentIndex += 1;
                    the.gotoEndIf();
                } else if (currentFunction.mark === "while")
                {
                    result = currentFunction.call();
                    if (result)
                    {
                        the.markEndWhile();
                        the.currentIndex += 1;
                    } else
                    {
                        the.currentIndex += 1;
                        the.gotoEndWhile();
                    }
                } else if (currentFunction.mark === "endwhile")
                {
                    startWhileFunction = the.functionList[currentFunction.startWhileIndex];
                    the.consoleInfo(currentFunction.startWhileIndex + " - " + startWhileFunction.mark);
                    result = startWhileFunction.call();
                    if (result)
                    {
                        the.currentIndex = currentFunction.startWhileIndex + 1;
                    } else
                    {
                        the.currentIndex += 1;
                    }
                } else
                {
                    currentFunction.call();
                    the.currentIndex += 1;
                    if (currentFunction.mark === "stop")
                    {
                        the.running = false;
                        the.consoleInfo("Call continue() to run next execution");
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
            gotoNextIf: function ()
            {
                stackCount = 0;
                while (this.currentIndex < this.functionList.length)
                {
                    currentFunction = this.functionList[this.currentIndex];
                    this.consoleInfo(this.currentIndex + " - " + currentFunction.mark);
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
            },
            gotoEndIf: function ()
            {
                stackCount = 0;
                while (this.currentIndex < this.functionList.length)
                {
                    currentFunction = this.functionList[this.currentIndex];
                    this.consoleInfo(this.currentIndex + " - " + currentFunction.mark);

                    if (currentFunction.mark === "if")
                    {
                        stackCount += 1;
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
                    }
                    this.currentIndex += 1;
                }
            },
            markEndWhile: function ()
            {
                var startWhileIndex = this.currentIndex;
                var index = this.currentIndex + 1;
                stackCount = 0;
                while (index < this.functionList.length)
                {
                    currentFunction = this.functionList[index];
                    this.consoleInfo(index + " - " + currentFunction.mark);

                    if (currentFunction.mark === "while")
                    {
                        stackCount += 1;
                    } else if (currentFunction.mark === "endwhile")
                    {
                        if (stackCount === 0)
                        {
                            currentFunction.startWhileIndex = startWhileIndex;
                            return;
                        } else
                        {
                            if (stackCount > 0)
                            {
                                stackCount -= 1;
                            }
                        }
                    }

                    index += 1;
                }
            },
            gotoEndWhile: function ()
            {
                stackCount = 0;
                while (this.currentIndex < this.functionList.length)
                {
                    currentFunction = this.functionList[this.currentIndex];
                    this.consoleInfo(this.currentIndex + " - " + currentFunction.mark);

                    if (currentFunction.mark === "while")
                    {
                        stackCount += 1;
                    } else if (currentFunction.mark === "endwhile")
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
                    }
                    this.currentIndex += 1;
                }
            }
        };

function mainInfo(text)
{
    console.info("[MAIN] " + text);
}

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

function _if(c)
{
    executor._if(c);
    var if_condition = {};
    if_condition._then = _then;
    return if_condition;
}

function _then(f)
{
    executor._do(f);
    var if_do = {};
    if_do._then = _then;
    if_do._elseif = _elseif;
    if_do._else = _else;
    if_do._endif = _endif;
    return if_do;
}

function _elseif(c)
{
    executor._elseif(c);
    var elseif = {};
    elseif._then = _then;
    elseif._elseif = _elseif;
    elseif._else = _else;
    elseif._endif = _endif;
    return elseif;
}

function _else(f)
{
    executor._else();
    executor._do(f);
    var else_do = {};
    else_do._then = _then;
    else_do._endif = _endif;
    return else_do;
}

function _endif()
{
    executor._endif();
    return "endif";
}

function _while(f)
{
    executor._while(f);
    var while_condition = {};
    while_condition._endwhile = _endwhile;
    while_condition._whiledo = _whiledo;
    return while_condition;
}

function _whiledo(f)
{
    executor._do(f);
    var while_do = {};
    while_do._endwhile = _endwhile;
    while_do._whiledo = _whiledo;
    return while_do;
}

function _endwhile()
{
    executor._endwhile();
    return "endwhile";
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
    counter = 1;

    // start registering execution blocks
    _do(() => {
        mainInfo("First");
        mainInfo("Another First");
    });
    _do(() => {
        mainInfo("Second");
        mainInfo("Another Second");
    });
    _wait(() => {
        mainInfo("Wait for 5 counter: " + counter);
        return (++counter > 5);
    });
    _do(() => {
        mainInfo("Third");
        counter = 1;
        mainInfo("Reset the counter to " + counter);
    });
    _wait(() => {
        mainInfo("Wait for 15 counter: " + counter);
        return (++counter > 15);
    });
    _do(() => {
        mainInfo("Done");
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
        mainInfo("get data from ajaxSample.html");
        xhttp = new XMLHttpRequest();
        xhttp.open("GET", "ajaxSample.html");
        xhttp.send();
    });

    // wait for ajax response ready
    _wait(() => {
        mainInfo("Wait for ajax status ready: " + xhttp.readyState);
        return (xhttp.readyState === 4);
    });

    _do(() => {
        mainInfo("Ajax Status: " + xhttp.status);
        mainInfo("Ajax Response: " + xhttp.responseText);
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
        mainInfo("get status from server");
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
        mainInfo("Ajax Status: " + xhttp.status);
        mainInfo("Ajax Response: " + xhttp.responseText);
    });

    return executor.start();
}

// If a block result true, then it run next block,
//  if not then run next "if" block or skip the block
function conditional_execution_if_only()
{
    // clear previous execution stacks
    executor.clear();

    // start registering execution blocks
    _do(() => {
        mainInfo("==== First Case if-only ====");
        counter = 1;
    });

    _if(() => {
        mainInfo("counter: " + counter);
        return counter++ === 1;
    })._then(() => {
        mainInfo("Execute this line");
    })._then(() => {
        mainInfo("and this line too");
    })._endif();

    _do(() => {
        mainInfo("==== Second Case if-only ====");
    });

    _if(() => {
        mainInfo("counter: " + counter);
        return counter++ === 1;
    })._then(() => {
        mainInfo("Never executed");
    })._then(() => {
        mainInfo("and this line too");
    })._endif();

    _do(() => {
        mainInfo("Done");
    });
    return executor.start();
}

// If a block result true, then it run next block,
//  if not then run next "else" block 
function conditional_execution_if_else()
{
    // clear previous execution stacks
    executor.clear();

    // start registering execution blocks
    _do(() => {
        mainInfo("==== Case if-else ====");
        counter = 2;
    });

    _if(() => {
        mainInfo("counter: " + counter);
        return counter++ === 1;
    })._then(() => {
        mainInfo("Never executed");
    })._else(() => {
        mainInfo("execute this else line");
    })._then(() => {
        mainInfo("and this else line too");
    })._endif();

    _do(() => {
        mainInfo("Done");
    });
    return executor.start();
}

// If a block result true, then it run next block,
//  if not then check next "elseif" block 
//  if not then check next "elseif" block and run if true.
//  if not then run next "else" block 
function conditional_execution_if_elseif_else()
{
    // clear previous execution stacks
    executor.clear();

    // start registering execution blocks
    _do(() => {
        mainInfo("==== First Case if-elseif-else ====");
        counter = 2;
    });

    _if(() => {
        mainInfo("counter: " + counter);
        return counter++ === 1;
    })._then(() => {
        mainInfo("Never executed");
    })._elseif(() => {
        mainInfo("counter: " + counter);
        return counter++ === 3;
    })._then(() => {
        mainInfo("execute this elseif line");
    })._then(() => {
        mainInfo("and this elseif line too");
    })._else(() => {
        mainInfo("Never executed");
    })._endif();

    _do(() => {
        mainInfo("==== Second Case if-elseif-else ====");
    });

    _if(() => {
        mainInfo("counter: " + counter);
        return counter++ === 1;
    })._then(() => {
        mainInfo("Never executed");
    })._elseif(() => {
        mainInfo("counter: " + counter);
        return counter++ === 3;
    })._then(() => {
        mainInfo("Never executed too");
    })._else(() => {
        mainInfo("execute this else line");
    })._then(() => {
        mainInfo("and this else line too");
    })._endif();

    _do(() => {
        mainInfo("Done");
    });
    return executor.start();
}

// while a block condition return true,
// repeat execution of group of blocks,
// which are marked by while-endwhile
function loop_execution_while()
{
    // clear previous execution stacks
    executor.clear();
    executor.silent();
    
    var nameList = ["Budi", "Agung", "Andi", "Harun", "Dina"];

    // start registering execution blocks
    _do(() => {
        mainInfo("==== loop while true ====");
        counter = 0;
    });

    _while(() => {
        mainInfo("counter: " + counter);
        return counter < nameList.length;
    })._whiledo(() => {
        mainInfo("Name-" + counter + ": " + nameList[counter]);
    })._whiledo(() => {
        mainInfo("Increment counter");
        counter += 1;
    })._endwhile();

    _do(() => {
        mainInfo("Done");
    });
    return executor.start();
}