package net.addictivesoftware.flow

import net.addictivesoftware.flow.objects.EventObject

/**
 * Trait that contains the (static) webpages
 */
trait WebPages {

  /**
   * Lists events
   */
  def listHtmlPage(events:List[EventObject]) =
    <html>
      <head>
        <title>Flow - List Events</title>
        <link href="/flow/css/main.css" rel="stylesheet" type="text/css"/>
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
