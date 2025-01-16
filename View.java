import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Clase que representa la ventana principal
class View extends JFrame {
    private final Controller controller;
    private Color color = Color.cyan;
    private final JTextField figureCounter = new JTextField();
    private boolean moveFigureOption;
    private boolean deleteFigureOption;
    private Figure figure;


    public View(Controller controller) {
        this.controller = controller; // Guardar referencia al controlador
        moveFigureOption = false;
        deleteFigureOption = false;

        JButton clearAllButton = new JButton("Clear window");
        JButton reorganizeSquaresButton = new JButton("Reorganize figures");
        JPanel colorLabel = new JPanel();
        JButton moveFigureButton = new JButton("Move figure");
        JButton deleteFigureButton = new JButton("Delete figure");
        JTextField statusField = new JTextField("No selected");

        colorLabel.setBackground(color);


        setTitle("Figure drawing");
        figureCounter.setText("Figures: 0");
        setFocusable(true);

        clearAllButton.addActionListener(e -> {
            controller.cleanSquareArray();
            figureCounter.setText("Figure:" + controller.getNumOfFigures());
            repaint();
        });

        reorganizeSquaresButton.addActionListener(e->{
            controller.reorganizeSquares(600, 600);
            repaint();
        });

        moveFigureButton.addActionListener(e-> {
            moveFigureOption = !moveFigureOption;
            statusField.setText(moveFigureOption ? "Please select a square": "Selection disabled");
        });//Alternar la opción de mover cuadrado

        deleteFigureButton.addActionListener(e-> {
            deleteFigureOption = !deleteFigureOption;
            statusField.setText(deleteFigureOption ? "Please select a square": "Selection disabled");
        });//Alternar la opción de borrar cuadrado



        add(colorLabel);
        add(clearAllButton);
        add(figureCounter);
        add(reorganizeSquaresButton);
        add(moveFigureButton);
        add(deleteFigureButton);
        add(statusField);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new FlowLayout());
        setSize(600, 600);

        // Agregar KeyBindings
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();

// Definir las teclas y las acciones
        inputMap.put(KeyStroke.getKeyStroke("UP"), "moveSquareUp");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "moveSquareDown");
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "moveSquareLeft");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "moveSquareRight");
        inputMap.put(KeyStroke.getKeyStroke("R"), "changeColorRed");
        inputMap.put(KeyStroke.getKeyStroke("G"), "changeColorGreen");
        inputMap.put(KeyStroke.getKeyStroke("B"), "changeColorBlue");
        inputMap.put(KeyStroke.getKeyStroke("Y"), "changeColorYellow");

// Asociar las acciones a las teclas
        actionMap.put("moveSquareUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (moveFigureOption && figure != null) {
                    // Implementa la lógica para mover el cuadrado hacia arriba
                    statusField.setText("Moving up");
                        figure.setY(figure.getY() > 4? (figure.getY() - 5) : 0);
                    repaint();
                }
            }
        });

        actionMap.put("moveSquareDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (moveFigureOption && figure != null) {
                    // Implementa la lógica para mover el cuadrado hacia abajo
                    statusField.setText("Moving down");
                        figure.setY(figure.getY() < 595? (figure.getY() + 5) : 600);
                    repaint();
                }
            }
        });

        actionMap.put("moveSquareLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (moveFigureOption && figure != null) {
                    // Implementa la lógica para mover el cuadrado hacia abajo
                    statusField.setText("Moving left");
                    figure.setX(figure.getX() > 5? (figure.getX() - 5) : 0);
                    repaint();
                }
            }
        });

        actionMap.put("moveSquareRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (moveFigureOption && figure != null) {
                    // Implementa la lógica para mover el cuadrado hacia abajo
                    statusField.setText("Moving right");
                    figure.setX(figure.getX() < 595? (figure.getX() + 5) : 600);
                    repaint();
                }
            }
        });

        actionMap.put("changeColorRed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = Color.RED;
                colorLabel.setBackground(color);
            }
        });

        actionMap.put("changeColorGreen", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = Color.GREEN;
                colorLabel.setBackground(color);
            }
        });

        actionMap.put("changeColorBlue", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = Color.BLUE;
                colorLabel.setBackground(color);
            }
        });

        actionMap.put("changeColorYellow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = Color.YELLOW;
                colorLabel.setBackground(color);
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(moveFigureOption || deleteFigureOption){
                    figure = controller.getFigureAt(e.getX(), e.getY());
                    if(figure != null){
                        statusField.setText("Figure selected");
                        if(deleteFigureOption){
                            controller.deleteFigure(figure);
                        }
                    }
                }else{
                    boolean filled = (e.getButton() == MouseEvent.BUTTON3); // Si se hizo clic con el botón derecho
                    controller.setColor(color);
                    controller.addFigure(e.getX(), e.getY(), filled); // Llama al controlador para agregar un cuadrado
                }
                repaint(); // Solicita repintar la ventana
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponents(g); // Limpia el panel antes de dibujar
        controller.drawSquare(g); // Pide al controlador que dibuje los cuadrados
        figureCounter.setText("Figures:" + controller.getNumOfFigures());
    }
}