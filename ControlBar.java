/**********************************************************************************************************************/
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

class ControlBar extends JPanel{
  protected Timer t;
  protected TimerControls tc;
  protected SaveControls sc;
  protected MainWindow parent;
  
  public MainWindow getParent(){return parent;}
  
  ControlBar(MainWindow creator){
    parent=creator;
    
    super.setPreferredSize(new Dimension(720,172));
    setLayout(new BorderLayout());
    
    tc=new TimerControls(this);
    sc=new SaveControls(parent.getGrid());
    
    add(tc,BorderLayout.WEST);
     add(sc,BorderLayout.EAST);
    
    super.repaint();
    super.setVisible(true);
        System.out.println("Done Constucting");
  }
  
}
/**********************************************************************************************************************/
class SaveControls extends JPanel implements ActionListener{
  private JButton save;
  private JButton load;
  private Grid grid;
  private ControlBar parent;
  
  SaveControls(Grid currentGrid){
    //parent=creator;
    grid=currentGrid; 
    super.setPreferredSize(new Dimension(200,172));
    setLayout(new FlowLayout());
    
    save=new JButton("Save");
    load=new JButton("Load");
    
    save.addActionListener(this);
    load.addActionListener(this);
    
    super.add(save);
    super.add(load);
    
    super.repaint();
    super.setVisible(true);


  }
  
  public void actionPerformed(ActionEvent e){
    
    if(e.getSource().equals(save))
      new GridSaver(grid,"name");
    if(e.getSource().equals(load)){
      GridLoader gl = new GridLoader(grid,"name");
      try{
        grid.setMap(gl.read());
      }catch (IOException ex){
        System.out.println("File load Failed");}
    }
    
  }
}
/**********************************************************************************************************************/
class TimerControls extends JPanel implements ActionListener, ChangeListener{
  private ControlBar parent;
  private JButton pausePlay;
  private JSlider speed;  
  
  
  TimerControls(ControlBar creator){
    parent=creator;
    super.setPreferredSize(new Dimension(200,172));
    setLayout(new BorderLayout());
    
    pausePlay=new JButton ("Play");
    speed=makeJSlider();
    
    pausePlay.addActionListener(this);
    speed.addChangeListener(this);
    
    super.add(pausePlay,BorderLayout.WEST);
    super.add(speed,BorderLayout.EAST);
    super.add(new JLabel(" Simulation Speed Controls"),BorderLayout.NORTH);
    
    super.repaint();
    super.setVisible(true);
  }
  
  public void actionPerformed(ActionEvent e){
    if (e.getSource().equals(pausePlay)){
      if(pausePlay.getText().equals("Play")){
        pausePlay.setText("Pause");
        Simulation.timer().start();
      }else{
        pausePlay.setText("Play");
        Simulation.timer().stop();
      } 
      
    }
  }
  
  public void stateChanged(ChangeEvent e) {
    JSlider source = (JSlider)e.getSource();
    if (!source.getValueIsAdjusting()) {
      int delay =1000* (int)source.getValue();
      Simulation.timer().setDelay(delay);
    }
  }
  
  
  private JSlider makeJSlider (){
    
    JSlider slider=new JSlider(JSlider.VERTICAL,0, 10, 1);
    slider.setMajorTickSpacing(1);
    slider.setPaintTicks(true);
    slider.setSnapToTicks(true);
    
    Hashtable labelTable = new Hashtable();
    for (int i=0;i<=10;i+=2){
      labelTable.put( new Integer(i), new JLabel(i+" Seconds") );
    }
    slider.setLabelTable(labelTable);
    slider.setPaintLabels(true);
    return slider;
  }
  
}