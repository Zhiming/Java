package nz.ac.unitec.zhiming.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import nz.ac.unitec.zhiming.constant.Shape;
import nz.ac.unitec.zhiming.model.ShapeEntity;
import nz.ac.unitec.zhiming.util.ClientTool;

public class CanvasUI extends JFrame {

	private static final long serialVersionUID = -7121737365591952855L;
	
	private JPanel pnlColor;
	private JPanel pnlShape;
	private JPanel pnlLeft;
	private JPanel pnlCanvas;
	private JPanel pnlChat;
	private TextArea content;
	private JTextField inputArea;
	private JButton btnSend;
	private JPanel pnlCenter;
	private JPanel pnlRight;
	private JScrollPane scrollPane;
	

	//这个变量用于保存画图时候的起点，因为在方法中的变量找不到，所以只能写在这里
	private Point tempStartPoint;
	private Point currentPoint;
	private HashSet<ShapeEntity> drawnShapes;
	private String usedShape;
	
	private ClientTool clientTool;
	
	private Color[] colors = { Color.GRAY, Color.BLACK, Color.BLUE, Color.CYAN,
			Color.DARK_GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA,
			Color.ORANGE, Color.PINK, Color.RED, Color.WHITE, Color.YELLOW,
			new Color(255, 51, 51), new Color(0, 204, 204),
			new Color(204, 0, 204), new Color(255, 153, 51),
			new Color(255, 153, 204) };
	//用于记录被点击的JPanel的颜色
	private Color currentColor;
	//用于记录那个颜色的JPanel被点击了
	private JPanel clickedColorPanel;
	//记录当前用那个颜色画图
	private JPanel usedPnlColor;
	
	
	public CanvasUI(ClientTool clientTool) {
		this();
		this.clientTool = clientTool;
		this.clientTool.bindCanvasUI(this);
	}

	public CanvasUI() {

//		startPoints = new LinkedList<Point>();
//		endPoints = new LinkedList<Point>();
		drawnShapes = new HashSet<ShapeEntity>();
		
		this.setSize(1000, 700);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Paint and Chat");
		this.setLocation(200, 40);
		this.setLayout(new BorderLayout());

		pnlLeft = new JPanel(new GridLayout(2, 1));
		pnlLeft.setPreferredSize(new Dimension(100, 700));

		pnlColor = new JPanel(new GridLayout(10, 2));
		setColorGrid(pnlColor);

		pnlShape = new JPanel(new GridLayout(3, 1));
		setShapeGrid(pnlShape);

		pnlLeft.add(pnlColor);
		pnlLeft.add(pnlShape);

		this.getContentPane().add(pnlLeft, BorderLayout.WEST);

		pnlCanvas = new JPanel();
		setCanvas(pnlCanvas);
		pnlCanvas.setPreferredSize(new Dimension(700, 450));

		pnlChat = new JPanel(new BorderLayout());
		content = new TextArea();
		content.setEditable(false);
		inputArea = new JTextField(30);
		btnSend = new JButton("Send");
		JPanel pnlInput = new JPanel();
		pnlInput.add(inputArea);
		pnlInput.add(btnSend);
		pnlChat.add(content, BorderLayout.CENTER);
		pnlChat.add(pnlInput, BorderLayout.SOUTH);

		pnlCenter = new JPanel();
		pnlCenter.add(pnlCanvas, BorderLayout.CENTER);
		pnlCenter.add(pnlChat, BorderLayout.SOUTH);

		this.add(pnlCenter, BorderLayout.CENTER);

		this.pnlRight = new JPanel();

		JPanel pnl = new JPanel();
		scrollPane = new JScrollPane(pnl);
		scrollPane.setPreferredSize(new Dimension(150, 300));
		pnlRight.add(scrollPane, BorderLayout.CENTER);

		this.add(pnlRight, BorderLayout.EAST);

		btnSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				broadcast();
			}
		});

		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {

			}

			@Override
			public void windowIconified(WindowEvent e) {

			}

			@Override
			public void windowDeiconified(WindowEvent e) {

			}

			@Override
			public void windowDeactivated(WindowEvent e) {

			}

			@Override
			public void windowClosing(WindowEvent e) {
				clientTool.closeClient();
				System.exit(0);
			}

			@Override
			public void windowClosed(WindowEvent e) {

			}

			@Override
			public void windowActivated(WindowEvent e) {

			}
		});

		this.setVisible(true);
	}

	public TextArea getContent() {
		return content;
	}
	
	private void broadcast() {
		String text = this.inputArea.getText();
		if (text != null && text != "" && text.length() != 0) {
			this.clientTool.broadcast(text);
			this.inputArea.setText("");
		}
	}

	private void setCanvas(JPanel pnlCanvas) {
		JPanel jcTemp = new JPanel() {
			
			private static final long serialVersionUID = 1807898649720296436L;

			{
				addMouseListener(new MouseAdapter() {

					@Override
					public void mousePressed(MouseEvent e) {
						Point p = e.getPoint();
						tempStartPoint = p;
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						Point p2 = e.getPoint();
						currentPoint = null;
						ShapeEntity se = new ShapeEntity();
						se.setShape(Shape.LINE);
						se.setStartPoint(tempStartPoint);
						se.setEndPoint(p2);
						se.setColor(usedPnlColor.getBackground());
						se.setShape(usedShape);
						drawnShapes.add(se);
						clientTool.synchronizeShape(drawnShapes);
					}
				});

				addMouseMotionListener(new MouseAdapter() {
					@Override
					public void mouseDragged(MouseEvent e) {
						currentPoint = e.getPoint();
						repaint();
					};
				});
			}
			
			private void drawLine(Graphics g, ShapeEntity se){
				g.drawLine(tempStartPoint.x, tempStartPoint.y, currentPoint.x, currentPoint.y);
			}
			
			private void drawCircle(Graphics g, ShapeEntity se){
				Point startPoint = se.getStartPoint();
				Point endPoint = se.getEndPoint();
				int radius =  (int)Math.sqrt(((startPoint.x-endPoint.x) * (startPoint.x-endPoint.x) + (startPoint.y-endPoint.y) * (startPoint.y-endPoint.y)));
				g.drawOval((startPoint.x - radius/2), (startPoint.y - radius/2), radius, radius);
			}
			
			private void drawRect(Graphics g, ShapeEntity se) {
				Point startPoint = se.getStartPoint();
				Point endPoint = se.getEndPoint();
				int width = Math.abs((startPoint.x - endPoint.x));
				int height = Math.abs((startPoint.y - endPoint.y));
				
				if (endPoint.x > startPoint.x) {
					if (endPoint.y > startPoint.y) {
						g.drawRect(startPoint.x, startPoint.y, width, height);
					} else {
						g.drawRect(startPoint.x, startPoint.y - height, width, height);
					}
				} else if(startPoint.x > endPoint.x){
					if (endPoint.y > startPoint.y) {
						g.drawRect(startPoint.x - width, startPoint.y, width, height);
					} else {
						g.drawRect(startPoint.x - width, startPoint.y - height, width, height);
					}
				}
			}

			private ShapeEntity wrapShapeEntity(Color color, Point startPoint, Point endPoint){
				ShapeEntity se = new ShapeEntity();
				se.setColor(color);
				se.setStartPoint(startPoint);
				se.setEndPoint(endPoint);
				return se;
			}
			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				this.setBackground(Color.WHITE);
				if (currentPoint != null) {
					ShapeEntity se = wrapShapeEntity(usedPnlColor.getBackground(), tempStartPoint, currentPoint);
					g.setColor(usedPnlColor.getBackground());
					if(usedShape.equals(Shape.LINE)){
						drawLine(g, se);
					}else if (usedShape.equals(Shape.CIRCLE)){
						drawCircle(g, se);
					}else if(usedShape.equals(Shape.RECT)){
						drawRect(g, se);
					}
				}
				
				for(ShapeEntity se : drawnShapes){
					String shape = se.getShape();
					g.setColor(se.getColor());
					if(shape.equals(Shape.LINE)){
						Point p1 = se.getStartPoint();
						Point p2 = se.getEndPoint();
						g.drawLine(p1.x, p1.y, p2.x, p2.y);
					}else if(shape.equals(Shape.CIRCLE)){
						drawCircle(g, se);
					}else if(shape.equals(Shape.RECT)){
						drawRect(g, se);
					}
				}
			}
		};

		jcTemp.setPreferredSize(new Dimension(800, 700));
		pnlCanvas.add(jcTemp);
	}
	
	public void paintDrawnShapes(HashSet<ShapeEntity> drawnShapes){
		this.drawnShapes = drawnShapes;
		repaint();
	}
	
	@Override
	public void paintComponents(Graphics g) {
		super.paintComponents(g);
		this.setBackground(Color.WHITE);
		for(ShapeEntity se : drawnShapes){
			if(se.getShape().equals(Shape.LINE)){
				
			}else if(se.getShape().equals(Shape.CIRCLE)){
				
			}else if(se.getShape().equals(Shape.RECT)){
				
			}
		}
	}

	private void setShapeGrid(JPanel pnlShape) {
		Image[] imgs = loadImages();

		class CustomPanel extends JPanel {
			private static final long serialVersionUID = 1L;
			private Image img;

			public CustomPanel(Image img) {
				this.img = img;
			}

			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(img, 0, 0, this);
			}
		}
		
		class CustShapeChangeMouseListener extends MouseAdapter{
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getSource().getClass() == CustomPanel.class){
					usedShape = ((CustomPanel)e.getSource()).getName();
				}
			}
		}

		if (imgs != null) {
			for(int i = 0; i < 3; i++){
				CustomPanel cp = new CustomPanel(imgs[i]);
				cp.setPreferredSize(new Dimension(100, 100));
				if(i == 0){
					cp.setName(Shape.LINE);
				}else if(i == 1){
					cp.setName(Shape.CIRCLE);
				}else if(i == 2){
					cp.setName(Shape.RECT);
				}
				cp.addMouseListener(new CustShapeChangeMouseListener());
				pnlShape.add(cp);
			}
		}
		
		usedShape = Shape.LINE;

	}
	
	private Image[] loadImages() {
		Image[] imgs = new Image[3];
		try {
			for(int i = 0; i < 3; i++){
				imgs[i] = ImageIO.read(new File("src/nz/ac/unitec/zhiming/resource/" + (i+1) + ".png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imgs;
	}

	private void setColorGrid(JPanel pnlColor) {
		
		class CustMouseListener extends MouseAdapter{
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if(e.getSource().getClass() == JPanel.class)	{
					clickedColorPanel = (JPanel)e.getSource();
					currentColor = clickedColorPanel.getBackground();
					usedPnlColor.setBackground(currentColor);
				}
			}
		}
		
		for (int i = 0; i < colors.length; i++) {
			JPanel colorPanel = new JPanel();
			colorPanel.setBackground(colors[i]);
			colorPanel.setPreferredSize(new Dimension(40, 40));
			colorPanel.addMouseListener(new CustMouseListener());
			pnlColor.add(colorPanel);
		}
		currentColor = Color.BLACK;
		usedPnlColor = new JPanel();
		usedPnlColor.setPreferredSize(new Dimension(40, 40));
		usedPnlColor.setBackground(currentColor);
		pnlColor.add(usedPnlColor);
		
	}

	public JPanel getPnlLeft() {
		return pnlLeft;
	}
	
	public JPanel getPnlCenter() {
		return pnlCenter;
	}
	
	public JPanel getPnlRight() {
		return pnlRight;
	}
	
	public JButton getBtnSend() {
		return btnSend;
	}
	
	public static void main(String[] args) {
		new CanvasUI();
	}
}
