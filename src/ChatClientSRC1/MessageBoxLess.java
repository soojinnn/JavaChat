package ChatClientSRC1;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

class MessageBoxLess extends JDialog implements ActionListener
{
  private Frame client;
  private Container c;

  public MessageBoxLess(JFrame parent, String title, String message){
    super(parent, true);
    setTitle(title);
    c = getContentPane();
    c.setLayout(null);
    JLabel lbl = new JLabel(message);
    lbl.setFont(new Font("SanSerif", Font.PLAIN, 12));
    lbl.setBounds(20, 10, 190, 20);
    c.add(lbl);

    JButton bt = new JButton("O K");
    bt.setBounds(60, 40, 70, 25);
    bt.setFont(new Font("SanSerif", Font.PLAIN, 12));
    bt.addActionListener(this);
    bt.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
    c.add(bt);

    Dimension dim = getToolkit().getScreenSize();
    setSize(200, 100);
    setLocation(dim.width/2 - getWidth()/2,
                dim.height/2 - getHeight()/2);
    show();
    client = parent;
  }
  public void actionPerformed(ActionEvent ae){
    dispose();
    System.exit(0);
  }
}
