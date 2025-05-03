package seman7;

import java.awt.EventQueue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Jugador {
    private String nombre;
    private String grupo;
    private String posicion;
    private int numero;

    int partidosJugados;
    int minutosTotales;
    int tries;
    int conversiones;
    int penales;
    int dropGoals;
    int faltas;
    int puntosTotales;

    public Jugador(String nombre, String grupo, String posicion, int numero) {
        this.nombre = nombre;
        this.grupo = grupo;
        this.posicion = posicion;
        this.numero = numero;
        this.partidosJugados = 0;
        this.minutosTotales = 0;
        this.tries = 0;
        this.conversiones = 0;
        this.penales = 0;
        this.dropGoals = 0;
        this.faltas = 0;
        this.puntosTotales = 0;
    }

    public void registrarEstadisticas(int minutos, int tries, int conversiones, int penales, int dropGoals, int faltas) {
        this.partidosJugados++;
        this.minutosTotales += minutos;
        this.tries += tries;
        this.conversiones += conversiones;
        this.penales += penales;
        this.dropGoals += dropGoals;
        this.faltas += faltas;

        this.puntosTotales += calcularPuntosPartido(tries, conversiones, penales, dropGoals);
    }

    public int calcularPuntosPartido(int tries, int conversiones, int penales, int dropGoals) {
        return (tries * 5) + (conversiones * 2) + (penales * 3) + (dropGoals * 3);
    }

    public double promedioMinutos() {
        if (partidosJugados == 0) return 0;
        return (double) minutosTotales / partidosJugados;
    }

    public double eficiencia() {
        if (minutosTotales == 0) return 0;
        return (double) puntosTotales / minutosTotales;
    }

    public String getNombre() {
        return nombre;
    }

    public String getGrupo() {
        return grupo;
    }

    public String getPosicion() {
        return posicion;
    }

    public int getNumero() {
        return numero;
    }

    @Override
    public String toString() {
        return nombre + " (" + posicion + " #" + numero + ")";
    }
}

public class final2 extends JFrame implements ActionListener {
    // TAB 1
    private static JTextField textField;
    private static JComboBox<Integer> cmbNumero;
    private static JComboBox<String> cmbPosicion, cmbGrupo;
    private static JTextArea txtResultado;
    private static JButton btnAgregar;
    private Set<Integer> numerosUsados = new HashSet<>();
    private Set<String> posicionesUsadas = new HashSet<>();
    private Map<String, Jugador> jugadoresRegistrados = new HashMap<>();

    // TAB 2
    private JComboBox<Jugador> cmbJugadores;
    private JTextField txtMinutos, txtTries, txtConversiones, txtPenales, txtDropGoals, txtFaltas;
    private JTextArea txtResumenEstadisticas;
    private JButton btnRegistrarEstadisticas;
    private int totalPartidosTemporada = 0;
    private int totalPuntosEquipo = 0;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
            	final2 frame = new final2();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public final2() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 700);
        JTabbedPane tabPanel = new JTabbedPane();
        getContentPane().add(tabPanel);

        JPanel page1 = createPage1Panel();
        tabPanel.addTab("Tab 1: Jugadores", page1);

        JPanel page2 = createPage2Panel();
        tabPanel.addTab("Tab 2: Estadísticas", page2);

        JPanel page3 = new JPanel();
        page3.add(new JLabel("Tab 3 (futuro)"));
        tabPanel.addTab("Tab 3", page3);
    }

    // TAB 1 - Crear Jugadores
    private JPanel createPage1Panel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblTitulo = new JLabel("Agregar Jugadores");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitulo.setBounds(120, 10, 250, 30);
        panel.add(lblTitulo);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(10, 60, 100, 20);
        panel.add(lblNombre);

        textField = new JTextField();
        textField.setBounds(120, 60, 250, 20);
        panel.add(textField);

        JLabel lblGrupo = new JLabel("Grupo:");
        lblGrupo.setBounds(10, 90, 100, 20);
        panel.add(lblGrupo);

        cmbGrupo = new JComboBox<>(new String[]{"Forwards", "Backs", "Suplentes"});
        cmbGrupo.setBounds(120, 90, 250, 25);
        panel.add(cmbGrupo);

        JLabel lblPosicion = new JLabel("Posición:");
        lblPosicion.setBounds(10, 130, 100, 20);
        panel.add(lblPosicion);

        cmbPosicion = new JComboBox<>();
        cmbPosicion.setBounds(120, 130, 250, 25);
        panel.add(cmbPosicion);

        JLabel lblNumero = new JLabel("Número:");
        lblNumero.setBounds(10, 170, 100, 20);
        panel.add(lblNumero);

        cmbNumero = new JComboBox<>();
        cmbNumero.setBounds(120, 170, 250, 25);
        panel.add(cmbNumero);

        btnAgregar = new JButton("Agregar");
        btnAgregar.setBounds(280, 210, 90, 25);
        btnAgregar.addActionListener(this);
        panel.add(btnAgregar);

        txtResultado = new JTextArea();
        JScrollPane scroll = new JScrollPane(txtResultado);
        scroll.setBounds(10, 250, 460, 350);
        panel.add(scroll);

        cmbGrupo.addActionListener(e -> {
            updatePositionComboBox();
            updateNumberComboBox();
        });

        updatePositionComboBox();
        updateNumberComboBox();
        return panel;
    }

    private JPanel createPage2Panel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblJugador = new JLabel("Jugador:");
        lblJugador.setBounds(10, 10, 80, 20);
        panel.add(lblJugador);

        cmbJugadores = new JComboBox<>();
        cmbJugadores.setBounds(100, 10, 300, 25);
        panel.add(cmbJugadores);

        String[] etiquetas = {"Minutos", "Tries", "Conversiones", "Penales", "Drop Goals", "Faltas"};
        JTextField[] campos = new JTextField[6];
        int y = 50;

        for (int i = 0; i < etiquetas.length; i++) {
            JLabel label = new JLabel(etiquetas[i] + ":");
            label.setBounds(10, y, 100, 20);
            panel.add(label);

            campos[i] = new JTextField();
            campos[i].setBounds(120, y, 100, 20);
            panel.add(campos[i]);

            y += 30;
        }

        txtMinutos = campos[0];
        txtTries = campos[1];
        txtConversiones = campos[2];
        txtPenales = campos[3];
        txtDropGoals = campos[4];
        txtFaltas = campos[5];

        btnRegistrarEstadisticas = new JButton("Registrar");
        btnRegistrarEstadisticas.setBounds(250, 200, 150, 25);
        panel.add(btnRegistrarEstadisticas);

        txtResumenEstadisticas = new JTextArea();
        JScrollPane scroll = new JScrollPane(txtResumenEstadisticas);
        scroll.setBounds(10, 240, 460, 370);
        panel.add(scroll);

        btnRegistrarEstadisticas.addActionListener(e -> registrarEstadisticasJugador());
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAgregar) {
            String nombre = textField.getText();
            Integer numero = (Integer) cmbNumero.getSelectedItem();
            String posicion = (String) cmbPosicion.getSelectedItem();
            String grupo = (String) cmbGrupo.getSelectedItem();

            if (nombre == null || nombre.isEmpty() || numero == null || posicion == null || grupo == null) {
                JOptionPane.showMessageDialog(null, "Complete todos los campos.");
                return;
            } else if (numerosUsados.contains(numero)) {
                JOptionPane.showMessageDialog(null, "Número ya usado.");
                return;
            } else if (posicionesUsadas.contains(posicion + grupo)) {
                JOptionPane.showMessageDialog(null, "Posición ya ocupada.");
                return;
            }

            // Crear el jugador
            Jugador jugador = new Jugador(nombre, grupo, posicion, numero);
            jugadoresRegistrados.put(nombre, jugador);
            numerosUsados.add(numero);
            posicionesUsadas.add(posicion + grupo);

            // Agregar el jugador al ComboBox en vez de su toString()
            cmbJugadores.addItem(jugador);

            txtResultado.append("✔ Jugador agregado: " + jugador.getNombre() + " - Posición: " + jugador.getPosicion() + " - Número: " + jugador.getNumero() + "\n");
            textField.setText("");
        }
    }

    private void registrarEstadisticasJugador() {
        Jugador jugador = (Jugador) cmbJugadores.getSelectedItem();
        if (jugador == null) {
            JOptionPane.showMessageDialog(null, "Seleccione un jugador.");
            return;
        }

        int minutos, tries, conversiones, penales, dropGoals, faltas;
        try {
            minutos = Integer.parseInt(txtMinutos.getText());
            tries = Integer.parseInt(txtTries.getText());
            conversiones = Integer.parseInt(txtConversiones.getText());
            penales = Integer.parseInt(txtPenales.getText());
            dropGoals = Integer.parseInt(txtDropGoals.getText());
            faltas = Integer.parseInt(txtFaltas.getText());

            if (minutos < 0 || tries < 0 || conversiones < 0 || penales < 0 || dropGoals < 0 || faltas < 0) {
                JOptionPane.showMessageDialog(null, "Valores negativos no permitidos.");
                return;
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Ingrese solo números válidos.");
            return;
        }

        // Registrar las estadísticas
        jugador.registrarEstadisticas(minutos, tries, conversiones, penales, dropGoals, faltas);
        totalPartidosTemporada++;
        totalPuntosEquipo += jugador.calcularPuntosPartido(tries, conversiones, penales, dropGoals);

        // Mostrar estadísticas del jugador
        txtResumenEstadisticas.setText("📊 Estadísticas de " + jugador + ":\n");
        txtResumenEstadisticas.append("Partidos jugados: " + jugador.partidosJugados + "\n");
        txtResumenEstadisticas.append("Minutos totales: " + jugador.minutosTotales + "\n");
        txtResumenEstadisticas.append("Promedio minutos por partido: " + String.format("%.2f", jugador.promedioMinutos()) + "\n\n");

        txtResumenEstadisticas.append("➡️ Tries (5 pts c/u): " + jugador.tries + "\n");
        txtResumenEstadisticas.append("➡️ Conversiones (2 pts c/u): " + jugador.conversiones + "\n");
        txtResumenEstadisticas.append("➡️ Penales (3 pts c/u): " + jugador.penales + "\n");
        txtResumenEstadisticas.append("➡️ Drop Goals (3 pts c/u): " + jugador.dropGoals + "\n");
        txtResumenEstadisticas.append("➡️ Faltas cometidas: " + jugador.faltas + "\n\n");

        txtResumenEstadisticas.append("Puntos totales jugador: " + jugador.puntosTotales + "\n");
        txtResumenEstadisticas.append("Eficiencia (puntos por minuto): " + String.format("%.2f", jugador.eficiencia()) + "\n\n");

        txtResumenEstadisticas.append("🏉 Total puntos del equipo: " + totalPuntosEquipo + "\n");
        txtResumenEstadisticas.append("📅 Total partidos en la temporada: " + totalPartidosTemporada + "\n");

        // Limpiar campos
        txtMinutos.setText("");
        txtTries.setText("");
        txtConversiones.setText("");
        txtPenales.setText("");
        txtDropGoals.setText("");
        txtFaltas.setText("");
    }

    private void updatePositionComboBox() {
        String grupo = (String) cmbGrupo.getSelectedItem();
        cmbPosicion.removeAllItems();

        switch (grupo) {

        case "Forwards":

        cmbPosicion.addItem("Pilar izquierdo (Prop)");

        cmbPosicion.addItem("Talonador (Hooker)");

        cmbPosicion.addItem("Pilar derecho (Prop)");

        cmbPosicion.addItem("Segunda línea izquierdo");

        cmbPosicion.addItem("Segunda línea derecho");

        cmbPosicion.addItem("ala lado ciego (izquiuerdo)");

        cmbPosicion.addItem("ala lado abierto (derecho)");

        cmbPosicion.addItem("Medio melé ");

        break;

        case "Backs":

        cmbPosicion.addItem("Medio scrum ");

        cmbPosicion.addItem("Apertura (Fly half)");

        cmbPosicion.addItem("Ala izquierdo (Left wing)");

        cmbPosicion.addItem("centro interior");

        cmbPosicion.addItem("centro exterior");

        cmbPosicion.addItem("Ala derecho (right wing)");

        cmbPosicion.addItem("Zaguero (Full back)");

        break;

        case "Suplentes":

        cmbPosicion.addItem("suplente 1");

        cmbPosicion.addItem("suplente 2");

        cmbPosicion.addItem("suplente 3");

        cmbPosicion.addItem("suplente 4");

        cmbPosicion.addItem("suplente 5");

        cmbPosicion.addItem("suplente 6");

        cmbPosicion.addItem("suplente 7");

        cmbPosicion.addItem("suplente 8");

        break;

        }

        }

    private void updateNumberComboBox() {
        String grupo = (String) cmbGrupo.getSelectedItem();
        cmbNumero.removeAllItems();

        switch (grupo) {

        case "Forwards":

        cmbNumero.addItem(1); 

        cmbNumero.addItem(2); 

        cmbNumero.addItem(3); 

        cmbNumero.addItem(4); 

        cmbNumero.addItem(5); 

        cmbNumero.addItem(6); 

        cmbNumero.addItem(7); 

        cmbNumero.addItem(8); 

        break;

        case "Backs":

        	 cmbNumero.addItem(9); 

        cmbNumero.addItem(10); 

        cmbNumero.addItem(11); 

        cmbNumero.addItem(12); 

        cmbNumero.addItem(13); 

        cmbNumero.addItem(14); 

        cmbNumero.addItem(15); 

        break;

        case "Suplentes":

        	 cmbNumero.addItem(16); 

        cmbNumero.addItem(17); 

        cmbNumero.addItem(18); 

        cmbNumero.addItem(19); 

        cmbNumero.addItem(20); 

        cmbNumero.addItem(21); 

        cmbNumero.addItem(22); 

        cmbNumero.addItem(23);

        break;

        }

        }

        }
