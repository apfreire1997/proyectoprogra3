import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GestorFinanzasGUI {
    private JTabbedPane tabgeneral;
    private JTextField txtusuario;
    private JPasswordField password;
    private JButton salirButton;
    private JButton iniciarSesionButton;
    private JPanel pGeneral;
    private JPanel login;
    private JComboBox cbfiltroegresos;
    private JButton filtrarButtonegresos;
    private JButton registrarConsumoButton;
    private JTextArea txtconsumos;
    private JTextField txtvalorconsumo;
    private JTextField txtmotivoconsumo;
    private JTextField txtvaloringreso;
    private JTextField txtmotivoingreso;
    private JComboBox cbfiltroingresos;
    private JButton filtrarButton;
    private JButton registrarIngresosButton;
    private JTextArea txtingresos;
    private JTextField txtvalormeta;
    private JTextField txtmotivometa;
    private JButton registrarMetaButton;
    private JTextField txtvaloraaportarmeta;
    private JButton aportarMetaButton;
    private JTextArea txtmeta;
    private JProgressBar pbmetas;
    private JPanel calendario;
    private JButton agregarRecordatorioButton;
    private JButton eliminarRecordatorioButton;
    private JComboBox cbfrecuenciameta;
    private JPanel jcalendar;
    private JTextArea txtcalendario;
    private JTextArea txtsaldodisponible;
    private JTextArea txtahorrometa;

    private String ingresosOriginal = "";
    private String consumosOriginal = "";
    private double metaValor = 0;
    private double aportesAcumulados = 0;

    public GestorFinanzasGUI() {
        tabgeneral.setEnabled(false); // Disable all tabs initially
        pbmetas.setMinimum(0);
        pbmetas.setMaximum(100);
        pbmetas.setValue(0);
        pbmetas.setStringPainted(true);

        // Actualizar el saldo disponible y el ahorro al inicio
        actualizarSaldoDisponible();
        actualizarAhorroMeta();

        iniciarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = txtusuario.getText();
                String contrasena = new String(password.getPassword());
                if (usuario.equals("admin") && contrasena.equals("admin")) {
                    tabgeneral.setEnabled(true);
                    tabgeneral.setSelectedIndex(1); // Switch to the main tab
                    JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso");
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
                }
            }
        });

        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        registrarConsumoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double valorConsumo = Double.parseDouble(txtvalorconsumo.getText());
                    String motivoConsumo = txtmotivoconsumo.getText();
                    String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    String consumoRegistro = "Consumo registrado: Valor: " + valorConsumo + " Motivo: " + motivoConsumo + " Fecha: " + fechaHora + "\n";
                    txtconsumos.append(consumoRegistro);
                    consumosOriginal += consumoRegistro;
                    txtvalorconsumo.setText("");
                    txtmotivoconsumo.setText("");

                    // Actualizar saldo disponible
                    actualizarSaldoDisponible();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un valor numérico válido para el consumo.");
                }
            }
        });

        registrarIngresosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double valorIngreso = Double.parseDouble(txtvaloringreso.getText());
                    String motivoIngreso = txtmotivoingreso.getText();
                    String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    String ingresoRegistro = "Ingreso registrado: Valor: " + valorIngreso + " Motivo: " + motivoIngreso + " Fecha: " + fechaHora + "\n";
                    txtingresos.append(ingresoRegistro);
                    ingresosOriginal += ingresoRegistro;
                    txtvaloringreso.setText("");
                    txtmotivoingreso.setText("");

                    // Actualizar saldo disponible
                    actualizarSaldoDisponible();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un valor numérico válido para el ingreso.");
                }
            }
        });

        registrarMetaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    metaValor = Double.parseDouble(txtvalormeta.getText());
                    String motivoMeta = txtmotivometa.getText();
                    String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    txtmeta.append("Meta registrada: Valor: $" + metaValor + " Motivo: " + motivoMeta + " Fecha: " + fechaHora + "\n");
                    txtvalormeta.setText("");
                    txtmotivometa.setText("");
                    aportesAcumulados = 0;
                    pbmetas.setValue(0);
                    pbmetas.setString("0%");

                    // Actualizar el campo de ahorro en metas
                    actualizarAhorroMeta();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un valor válido para la meta.");
                }
            }
        });

        aportarMetaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double valorAportar = Double.parseDouble(txtvaloraaportarmeta.getText());
                    aportesAcumulados += valorAportar;
                    String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    txtmeta.append("Aporte a meta: Valor: $" + valorAportar + " Fecha: " + fechaHora + "\n");
                    txtvaloraaportarmeta.setText("");

                    // Update progress bar
                    int progreso = (int) ((aportesAcumulados / metaValor) * 100);
                    pbmetas.setValue(progreso);
                    pbmetas.setString(progreso + "%");

                    // Actualizar el campo de ahorro en metas
                    actualizarAhorroMeta();

                    // Check if goal is met
                    if (aportesAcumulados >= metaValor) {
                        int respuesta = JOptionPane.showConfirmDialog(null, "Meta conseguida. ¿Desea agregar otra meta?", "Meta conseguida", JOptionPane.YES_NO_OPTION);
                        if (respuesta == JOptionPane.YES_OPTION) {
                            txtmeta.setText("");
                            pbmetas.setValue(0);
                            pbmetas.setString("0%");
                            aportesAcumulados = 0;
                            metaValor = 0;

                            // Actualizar el campo de ahorro en metas
                            actualizarAhorroMeta();
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un valor válido para el aporte.");
                }
            }
        });
    }

    // Método para calcular y actualizar el saldo disponible
    private void actualizarSaldoDisponible() {
        double totalIngresos = sumarValores(ingresosOriginal);
        double totalEgresos = sumarValores(consumosOriginal);
        double saldoDisponible = totalIngresos - totalEgresos;

        txtsaldodisponible.setText("Saldo Disponible: $" + saldoDisponible);
    }

    // Método para calcular y actualizar el progreso acumulado en las metas
    private void actualizarAhorroMeta() {
        if (metaValor > 0) {
            double porcentajeAhorro = (aportesAcumulados / metaValor) * 100;
            txtahorrometa.setText("Ahorro actual: $" + aportesAcumulados + " (" + String.format("%.2f", porcentajeAhorro) + "% de la meta)");
        } else {
            txtahorrometa.setText("No hay metas activas.");
        }
    }

    // Método para sumar los valores de una cadena de texto
    private double sumarValores(String registros) {
        double suma = 0.0;
        String[] lines = registros.split("\n");
        for (String line : lines) {
            if (line.contains("Valor: ")) {
                try {
                    String valorStr = line.split("Valor: ")[1].split(" ")[0];
                    suma += Double.parseDouble(valorStr);
                } catch (Exception ex) {
                    // Ignorar líneas con formato incorrecto
                }
            }
        }
        return suma;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Gestor de Finanzas");
        frame.setContentPane(new GestorFinanzasGUI().pGeneral);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
