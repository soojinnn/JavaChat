package ChatClientSRC1;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

class ChatRoomDisplay extends JFrame implements ActionListener, KeyListener,
                                                ListSelectionListener, ChangeListener
{
	  private static final String INIT_TXT = "Trail: Creating a GUI with JFC/Swing\n"
		      + "https://docs.oracle.com/javase/tutorial/uiswing/learn/index.html\n"
		      + "Lesson: Learning Swing by Example\n"
		      + "This lesson explains the concepts you need to\n"
		      + " use Swing components in building a user interface.\n"
		      + " First we examine the simplest Swing application you can write.\n"
		      + " Then we present several progressively complicated examples of creating\n"
		      + " user interfaces using components in the javax.swing package.\n"
		      + " We cover several Swing components, such as buttons, labels, and text areas.\n"
		      + " The handling of events is also discussed,\n"
		      + " as are layout management and accessibility.\n"
		      + " This lesson ends with a set of questions and exercises\n"
		      + " so you can test yourself on what you've learned.\n"
		      + "https://docs.oracle.com/javase/tutorial/uiswing/learn/index.html\n";

  private ClientThread cr_thread;
  private String idTo;
  private boolean isSelected;
  public boolean isAdmin;

  private JLabel roomer;
  public JList roomerInfo;
  private JButton coerceOut, sendWord, sendFile, quitRoom, prevButton, nextButton;
  private Font font;
  private JViewport view;
  private JScrollPane jsp3;
  public JTextArea messages;
  public JTextField message;
  public JTextField messageSearch;
  
  public ChatRoomDisplay(ClientThread thread){
    super("Chat-Application-대화방");

    cr_thread = thread;
    isSelected = false;
    isAdmin = false;
    font = new Font("SanSerif", Font.PLAIN, 12);

    Container c = getContentPane();
    c.setLayout(null);

    JPanel fp = new JPanel();
    fp.setLayout(null);
    fp.setBounds(9, 12, 553, 53);
    fp.setBorder(new TitledBorder(
    		new EtchedBorder(EtchedBorder.LOWERED), "검색"));
    c.add(fp);
    
    messageSearch = new JTextField();
    messageSearch.setFont(font);
    messageSearch.addKeyListener(this);
    messageSearch.setBounds(14, 21, 380, 20);
    messageSearch.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
    fp.add(messageSearch);
    
    prevButton = new JButton("△");
    prevButton.setBounds(431, 16, 52, 30);
    fp.add(prevButton);
    prevButton.setFont(font);
    prevButton.addActionListener(this);
    prevButton.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
    
    nextButton = new JButton("▽");
    nextButton.setBounds(487, 16, 52, 30);
    fp.add(nextButton);
    nextButton.setFont(font);
    nextButton.addActionListener(this);
    nextButton.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
    
    JPanel p = new JPanel();
    p.setLayout(null);
    p.setBounds(422, 77, 140, 175);
    p.setBorder(new TitledBorder(
      new EtchedBorder(EtchedBorder.LOWERED), "참여자"));


    roomerInfo = new JList();
    roomerInfo.setFont(font);
    JScrollPane jsp2 = new JScrollPane(roomerInfo);
    roomerInfo.addListSelectionListener(this);
    jsp2.setBounds(15, 25, 110, 135);
    p.add(jsp2);

    c.add(p);

    p = new JPanel();
    p.setLayout(null);
    p.setBounds(9, 77, 410, 340);
    p.setBorder(new TitledBorder(
      new EtchedBorder(EtchedBorder.LOWERED), "채팅창"));

    view = new JViewport();
    messages = new JTextArea();
    messages.setFont(font);
    messages.setLineWrap(true); //줄넘김 추가
    messages.setEditable(false);
    view.add(messages);
    view.addChangeListener(this);
    jsp3 = new JScrollPane(view);
    jsp3.setBounds(15, 25, 380, 270);
    messages.setText(INIT_TXT);
    p.add(jsp3);

    message = new JTextField();
    message.setFont(font);
    message.addKeyListener(this);
    message.setBounds(15, 305, 380, 20);
    message.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
    p.add(message);

    c.add(p);

    coerceOut = new JButton("강 제 퇴 장");
    coerceOut.setFont(font);
    coerceOut.addActionListener(this);
    coerceOut.setBounds(442, 262, 100, 30);
    coerceOut.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
    c.add(coerceOut);

    sendWord = new JButton("귓말보내기");
    sendWord.setFont(font);
    sendWord.addActionListener(this);
    sendWord.setBounds(442, 302, 100, 30);
    sendWord.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
    c.add(sendWord);

    sendFile = new JButton("파 일 전 송");
    sendFile.setFont(font);
    sendFile.addActionListener(this);
    sendFile.setBounds(442, 342, 100, 30);
    sendFile.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
    c.add(sendFile);

    quitRoom = new JButton("퇴 실 하 기");
    quitRoom.setFont(font);
    quitRoom.addActionListener(this);
    quitRoom.setBounds(442, 382, 100, 30);
    quitRoom.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
    c.add(quitRoom);

    Dimension dim = getToolkit().getScreenSize();
    setSize(584, 474);
    setLocation(dim.width/2 - getWidth()/2,
                dim.height/2 - getHeight()/2);
    show();

    addWindowListener(
      new WindowAdapter() {
        public void windowActivated(WindowEvent e) {
          message.requestFocusInWindow();
        }
      }
    );

    addWindowListener(
      new WindowAdapter(){
        public void windowClosing(WindowEvent e){
          cr_thread.requestQuitRoom();
        }
      }
    );
  }

  public void resetComponents(){
    messages.setText("");
    message.setText("");
    message.requestFocusInWindow();
  }

  public void keyPressed(KeyEvent ke){
    if (ke.getKeyChar() == KeyEvent.VK_ENTER){
      String words = message.getText();
      String data;
      String idTo;
      if(words.startsWith("/w")){
        StringTokenizer st = new StringTokenizer(words, " ");
        String command = st.nextToken();
        idTo = st.nextToken();
        data = st.nextToken();
        cr_thread.requestSendWordTo(data, idTo);
        message.setText("");
      } else {
        cr_thread.requestSendWord(words);
        message.requestFocusInWindow();
      }
    }
  }

  public void valueChanged(ListSelectionEvent e){
    isSelected = true;
    idTo = String.valueOf(((JList)e.getSource()).getSelectedValue());
  }

  public void actionPerformed(ActionEvent ae){
    if (ae.getSource() == coerceOut) {
      if (!isAdmin) {
        JOptionPane.showMessageDialog(this, "당신은 방장이 아닙니다.",
                        "강제퇴장", JOptionPane.ERROR_MESSAGE);
      } else if (!isSelected) {
        JOptionPane.showMessageDialog(this, "강제퇴장 ID를 선택하세요.",
                        "강제퇴장", JOptionPane.ERROR_MESSAGE);
      } else {
        cr_thread.requestCoerceOut(idTo);
        isSelected = false;
      }
    } else if (ae.getSource() == quitRoom) {
      cr_thread.requestQuitRoom();
    } else if (ae.getSource() == sendWord) {
      String idTo, data;
      if ((idTo = JOptionPane.showInputDialog("아이디를 입력하세요.")) != null){
        if ((data = JOptionPane.showInputDialog("메세지를 입력하세요.")) != null) {
          cr_thread.requestSendWordTo(data, idTo);
        }
      }
    } else if (ae.getSource() == sendFile) {
      String idTo;
      if ((idTo = JOptionPane.showInputDialog("상대방 아이디를 입력하세요.")) != null){
        cr_thread.requestSendFile(idTo);
      }
    }
  }
  
  public void stateChanged(ChangeEvent e){
    jsp3.getVerticalScrollBar().setValue((jsp3.getVerticalScrollBar().getValue() + 20));    	
  }
  public void keyReleased(KeyEvent e){}
  public void keyTyped(KeyEvent e){}
}
