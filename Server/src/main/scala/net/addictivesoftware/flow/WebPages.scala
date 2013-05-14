package net.addictivesoftware.flow

import net.addictivesoftware.flow.objects.WebEvent

/**
 * Trait that contains the (static) webpages
 */
trait WebPages {

  /**
   * Index page, lists API methods
   */
  val index =
    <html>
      <head>
        <title>Flow - Index</title>
        <link href="css/main.css" rel="stylesheet" type="text/css"/>
      </head>
      <body>
        <h3>Flow: a "Non Blocking" webservice to record events:</h3>
        <table border="1">
          <tr>
            <th>What?</th>
            <th>Method</th>
            <th>Url</th>
            <th>Description</th>
          </tr>
          <tr>
            <td>This file</td>
            <td>GET</td>
            <td><a href="/flow/index">/flow/index</a></td>
            <td></td>
          </tr>
          <tr>
            <td>Get a session ID</td>
            <td>GET</td>
            <td><a href ="/flow/getsession">/flow/getsession</a></td>
            <td><i>This is optional, it's fine to pass on the session id from the webserver</i></td>
          </tr>
          <tr>
            <td>Record an Event</td>
            <td>POST</td>
            <td><a href="/flow/event/[session]/[event-name]">/flow/event/[session]/[event-name]</a></td>
            <td>post parameters will also be stored</td>
          </tr>
          <tr>
            <td>List Events</td>
            <td>GET</td>
            <td><a href="/flow/list">/flow/list</a></td>
            <td>List currently stored events</td>
          </tr>
          <tr>
            <td>List Events</td>
            <td>GET</td>
            <td><a href="/flow/list/[session]">/flow/list/[session]</a></td>
            <td>List currently stored events for a session</td>
          </tr>
        </table>

        <p>A Test Html is available here: <a href="/flow/test.html">/flow/test.html</a></p>

      </body>
    </html>

  /**
   * Lists events
   */
  def list(events:List[WebEvent]) =
    <html>
      <head>
        <title>Flow - List Events</title>
        <link href="css/main.css" rel="stylesheet" type="text/css"/>
      </head>
      <body>
        <h3>List Events:</h3>
        <table class="event-list">
          <tr>
            <th>TimeStamp</th>
            <th>id</th>
            <th>Session</th>
            <th>Event</th>
            <th>Data</th>
          </tr>
          {
          events.map(
            event => {
              <tr>
                <td>{event.timestamp}</td>
                <td>{event._id}</td>
                <td>{event.session}</td>
                <td>{event.event}</td>
                <td>
                  <table class="keyvalue-list">
                    {event.data.map(
                    entry =>
                      <tr>
                        <td>{entry._1}</td>
                        <td>{entry._2}</td>
                      </tr>
                  )
                    }
                  </table>
                </td>
              </tr>
            }
          )
          }
        </table>
      </body>
    </html>
}
