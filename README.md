## Play Akka Parent-Child Example

A Play Framework application demonstrating dependency injection of Parent-child actors.

The `Parent` actor redirects messages for a given child based on recipient's `username`, if there's no `Child` actor for the `username`, the `Parent` creates one and then redirects the message.

A message can be send as a *HTTP POST* to `/:username/message` with A JSON body like:
```json
{
  "message": "hi, user"
}
```