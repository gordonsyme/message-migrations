# message-migrations

A repository to demonstrate how message formats can be evolved over time,
assuming you are in a position to get clients to upgrade.

## Usage

Start the server in a repl:
```
$ lein repl

message-migrations.core=> (require '[message-migrations.server :as server])
nil
message-migrations.core=> (def http-server (server/run-in-repl))
#'message-migrations.core/http-server
```

You can stop the server by running `(.stop http-server)`

In another terminal window run `git rebase -i --root` and change "pick" to
"reword" for each line.
This will get `git` to step through each commit and show you the commit message
as you go.
The commit messages have `curl` commands that demonstrate each step of the
process.

At each step reload the message processing code to get the new behaviour
In your repl run:
```
message-migrations.core=> (require 'message-migrations.core :reload)
nil
```
