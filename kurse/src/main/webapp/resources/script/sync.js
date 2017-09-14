var executor =
        {
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
                this.currentIndex = 0;
                setTimeout(this.run, 0);
                return this.functionList.length;
            },
            run: function (the)
            {
                the = executor;
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
                }

                if (the.currentIndex >= the.functionList.length)
                {
                    return;
                }
                // Progresif wait, first 100ms, then 200ms, 300ms, ...
                setTimeout(the.run, 100 * (the.waitCounter));
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

var counter = 1;

// sample usage
function ncps()
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