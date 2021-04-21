package ChatClientSRC1;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

class MessageBox extends JDialog implements ActionListener
{
  private Container c;
  private JButton bt;

  public MessageBox(JFrame parent, String title, String message){
    super(parent, false);
    setTitle(title);
    c = getContentPane();
    c.setLayout(null);
    JLabel lbl = new JLabel(message);
    lbl.setFont(new Font("SanSerif", Font.PLAIN, 12));
    lbl.setBounds(20, 10, 190, 20);
    c.add(lbl);

    bt = new JButton("»Æ ¿Œ");
    bt.setBounds(60, 40, 70, 25);
    bt.setFont(new Font("SanSerif", Font.PLAIN, 12));
    bt.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
    bt.addActionListener(this);
    c.add(bt);

    Dimension dim = getToolkit().getScreenSize();
    setSize(200, 100);
    setLocation(dim.width/2 - getWidth()/2,
                dim.height/2 - getHeight()/2);
    show();
    addWindowListener(
      new WindowAdapter(){
        public void windowClosing(WindowEvent e){
          dispose();
        }
      }
    );
  }

  public void actionPerformed(ActionEvent ae){
    if(ae.getSource() == bt){
      dispose();
    }
  }
}
