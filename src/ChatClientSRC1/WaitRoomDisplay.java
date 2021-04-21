package ChatClientSRC1;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

class WaitRoomDisplay extends JFrame implements ActionListener,KeyListener,
                                                MouseListener, ChangeListener
{
  private ClientThread cc_thread;
  private int roomNumber;
  private String password, select;
  private boolean isRock, isSelected;

  private JLabel rooms, waiter, label;
  public JList roomInfo, waiterInfo;
  private JButton create, join, sendword, logout;
  private Font font;
  private JViewport view;
  private JScrollPane jsp3;
  public JTextArea messages;
  public JTextField message;
  
  public WaitRoomDisplay(ClientThread thread){
    super("Chat-Application-대기실");

    cc_thread = thread;
    roomNumber = 0;
    password = "0";
    isRock = false;
    isSelected = false;
    font = new Font("SanSerif", Font.PLAIN, 12);

    Container c = getContentPane();
    c.setLayout(null);

    rooms = new JLabel("대화방");

    JPanel p = new JPanel();
    p.setLayout(null);
    p.setBounds(5, 10, 460, 215);
    p.setFont(font);
    p.setBorder(new TitledBorder(
      new EtchedBorder(EtchedBorder.LOWERED), "대화방 목록"));

    label = new JLabel("번 호");
    label.setBounds(15, 25, 40, 20);
    label.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
    label.setFont(font);
    p.add(label);

    label = new JLabel("제 목");
    label.setBounds(55, 25, 210, 20);
    label.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
    label.setFont(font);
    p.add(label);

    label = new JLabel("현재/최대");
    label.setBounds(265, 25, 60, 20);
    label.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
    label.setFont(font);
    p.add(label);

    label = new JLabel("공개여부");
    label.setBounds(325, 25, 60, 20);
    label.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
    label.setFont(font);
    p.add(label);

    label = new JLabel("개 설 자");
    label.setBounds(385, 25, 58, 20);
    label.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
    label.setFont(font);
    p.add(label);

    roomInfo = new JList();
    roomInfo.setFont(font);
    WaitListCellRenderer renderer = new WaitListCellRenderer();
    JScrollPane jsp1 = new JScrollPane(roomInfo);
    roomInfo.addMouseListener(this);
    renderer.setDefaultTab(20);
    renderer.setTabs(new int[]{40, 265, 285, 315, 375, 430});
    roomInfo.setCellRenderer(renderer);
    jsp1.setBounds(15, 45, 430, 155);
    p.add(jsp1);

    c.add(p);

    p = new JPanel();
    p.setLayout(null);
    p.setBounds(470, 10, 150, 215);
    p.setBorder(new TitledBorder(
      new EtchedBorder(EtchedBorder.LOWERED), "대기자"));

    waiterInfo = new JList();
    waiterInfo.setFont(font);
    JScrollPane jsp2 = new JScrollPane(waiterInfo);
    jsp2.setBounds(15, 25, 115, 175);
    p.add(jsp2);

    c.add(p);

    p = new JPanel();
    p.setLayout(null);
    p.setBounds(5, 230, 460, 200);
    p.setBorder(new TitledBorder(
      new EtchedBorder(EtchedBorder.LOWERED), "채팅창"));

    view = new JViewport();
    messages = new JTextArea();
    messages.setEditable(false);
    messages.setLineWrap(true); //줄넘김 
    messages.setFont(font);   
    view.add(messages);
    view.addChangeListener(this);
    jsp3 = new JScrollPane(view);
    jsp3.setBounds(15, 25, 430, 135);
    view.addChangeListener(this);
    p.add(jsp3);
    
    view = (JViewport) jsp3.getViewport().getView();
    view.addChangeListener(this);

    message = new JTextField();
    message.setFont(font);
    message.setBounds(15, 170, 430, 20);
    message.addKeyListener(this);
    message.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
    p.add(message);

    c.add(p);

    create = new JButton("대화방개설");
    create.setFont(font);
    create.setBounds(500, 250, 100, 30);
    create.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
    create.addActionListener(this);
    c.add(create);

    join = new JButton("대화방참여");
    join.setFont(font);
    join.setBounds(500, 290, 100, 30);
    join.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
    join.addActionListener(this);
    c.add(join);

    sendword = new JButton("귓말보내기");
    sendword.setFont(font);
    sendword.setBounds(500, 330, 100, 30);
    sendword.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
    sendword.addActionListener(this);
    c.add(sendword);

    logout = new JButton("로 그 아 웃");
    logout.setFont(font);
    logout.setBounds(500, 370, 100, 30);
    logout.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
    logout.addActionListener(this);
    c.add(logout);

    Dimension dim = getToolkit().getScreenSize();
    setSize(640, 460);
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
          cc_thread.requestLogout();
        }
      }
    );
  }

  public void resetComponents(){
    messages.setText("");
    message.setText("");
    roomNumber = 0;
    password = "0";
    isRock = false;
    isSelected = false;
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
        cc_thread.requestSendWordTo(data, idTo);
        message.setText("");
      } else {
        cc_thread.requestSendWord(words);
        message.requestFocusInWindow();
      }
    }
  }

  public void mouseClicked(MouseEvent e){
    try{
      isSelected = true;
      String select = String.valueOf(((JList)e.getSource()).getSelectedValue());
      setSelectedRoomInfo(select);
    }catch(Exception err){}
  }

  public void actionPerformed(ActionEvent ae){
    if(ae.getSource() == create){
      CreateRoomDisplay createRoom = new CreateRoomDisplay(this, cc_thread);
    } else if(ae.getSource() == join){     
      if(!isSelected){
        JOptionPane.showMessageDialog(this, "입장할 방을 선택하세요.",
                    "대화방 입장.", JOptionPane.ERROR_MESSAGE);
      } else if(isRock && password.equals("0")){
        if ((password = JOptionPane.showInputDialog("비밀번호를 입력하세요.")) != null){
          if (!password.equals("")){
            cc_thread.requestEnterRoom(roomNumber, password);
            password = "0";
          } else {
            password = "0";
            cc_thread.requestEnterRoom(roomNumber, password);
          }
        } else {
          password = "0";
        }
      } else {
        cc_thread.requestEnterRoom(roomNumber, password);
      }
    } else if(ae.getSource() == logout){
      cc_thread.requestLogout();
    } else if(ae.getSource() == sendword){
      String idTo, data;
      if ((idTo = JOptionPane.showInputDialog("아이디를 입력하세요.")) != null){ 
        if ((data = JOptionPane.showInputDialog("메세지를 입력하세요.")) != null){
          cc_thread.requestSendWordTo(data, idTo);
        }
      }
    }

  }
      
  private void setSelectedRoomInfo(String select){
    StringTokenizer st = new StringTokenizer(select, "=");
    roomNumber = Integer.parseInt(st.nextToken());
    String roomName = st.nextToken();
    int maxUser = Integer.parseInt(st.nextToken());
    int user = Integer.parseInt(st.nextToken());
    isRock = st.nextToken().equals("비공개") ? true : false;
  }
  
  public void stateChanged(ChangeEvent e){
    jsp3.getVerticalScrollBar().setValue((jsp3.getVerticalScrollBar().getValue() + 20));
  }
  
  public void keyReleased(KeyEvent e){}
  public void keyTyped(KeyEvent e){}
  public void mousePressed(MouseEvent e){}
  public void mouseReleased(MouseEvent e){}
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e){}
}
