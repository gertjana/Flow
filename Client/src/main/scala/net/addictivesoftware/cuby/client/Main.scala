import java.awt.BorderLayout
import javax.swing._
import swing._

object Client extends SimpleSwingApplication {
  def maxWidth = 640
  def maxHeight = 480
  def HeightInputPanel = 40
  def widthButton = 80
  def initXPos = 320                                                                                                        +
  def initYPos = 260

  val inputField = new JTextField()
  inputField.setPreferredSize(new Dimension(maxWidth-widthButton,20))
  inputField.setMaximumSize(new Dimension(maxWidth-widthButton, 20))

  val textArea = new JTextArea("Welcome to Cuby\r\nConnecting to server....")
  textArea.setPreferredSize(new Dimension(maxWidth,maxHeight-HeightInputPanel))
  textArea.setEditable(false)


  def sendButton = {
    val b = new Button("send") {
      sendMessageToServer(inputField.getText())
    }
    b.peer.setPreferredSize(new Dimension(widthButton,HeightInputPanel))
    b.peer
  }

  def inputPanel = {
    val ip = new JPanel()
    ip.setPreferredSize(new Dimension(maxWidth, HeightInputPanel))
    ip.setLayout(new BorderLayout())
    ip.add(inputField, BorderLayout.WEST)
    ip.add(sendButton, BorderLayout.EAST)
    ip
  }

  def panel = {
    val p = new JPanel()
    p.setLayout(new BorderLayout())
    p.setPreferredSize(new Dimension(maxWidth,maxHeight))
    p.add(textArea, BorderLayout.NORTH)
    p.add(inputPanel, BorderLayout.SOUTH)
    p
  }


  def top = new MainFrame {
    peer.setLocation(initXPos, initYPos)
    title = "Cuby Client"

    contents = new Panel {
      preferredSize = new Dimension(maxWidth, maxHeight)
      focusable = true
      peer.add(panel)
      pack()
    }
  }

  private def sendMessageToServer(message:String) = {
    println("sending:" + message)
  }

}