package formulario;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class formularioContacto extends JFrame {

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtDNI;
    private JTextField txtPasaporte;
    private JTextField txtTelefono;
    private JTextField txtCodigoPostal;
    private JTextField txtDomicilio;

    private static final Color COLOR_ERROR = new Color(255, 200, 200);
    private static final Color COLOR_OK = new Color(200, 255, 200);
    private static final Color COLOR_NORMAL = Color.WHITE;

    public formularioContacto() {
        super("Carga de Contacto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        construirUI();
        pack();
        setLocationRelativeTo(null);
    }

    //interfaz
    private void construirUI() {
        //panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        //título
        JLabel titulo = new JLabel("Carga de Contacto", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        //panel de campos
        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //Fila 0: Nombre
        txtNombre = new JTextField(22);
        aplicarFiltro(txtNombre, 20, filtroTexto.Tipo.SOLO_ALFABETICO);
        agregarFila(panelCampos, gbc, 0, "Nombre: *", txtNombre, "(máx. 20 caracteres alfabéticos)");

        //Fila 1: Apellido
        txtApellido = new JTextField(22);
        aplicarFiltro(txtApellido, 20, filtroTexto.Tipo.SOLO_ALFABETICO);
        agregarFila(panelCampos, gbc, 1, "Apellido: *", txtApellido, "(máx. 20 caracteres alfabéticos)");

        //Fila 2: DNI
        txtDNI = new JTextField(22);
        aplicarFiltro(txtDNI, 8, filtroTexto.Tipo.SOLO_NUMERICO);
        agregarFila(panelCampos, gbc, 2, "DNI:", txtDNI, "(8 dígitos, 10.000.000 - 60.000.000)");

        //Fila 3: Pasaporte
        txtPasaporte = new JTextField(22);
        aplicarFiltro(txtPasaporte, 9, filtroTexto.Tipo.ALFANUMERICO_PASAPORTE);
        agregarFila(panelCampos, gbc, 3, "Pasaporte:", txtPasaporte, "(1 letra + 8 dígitos, ej: N39392288)");

        //mejora: listener para marcar que son excluyentes
        txtDNI.addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) {
                verificarExclusividadDocumento();
            }
        });
        txtPasaporte.addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) {
                verificarExclusividadDocumento();
            }
        });

        //Fila 4: Teléfono
        txtTelefono = new JTextField(22);
        aplicarFiltro(txtTelefono, 20, filtroTexto.Tipo.TELEFONO);
        agregarFila(panelCampos, gbc, 4, "Teléfono: *", txtTelefono, "(> 6 dígitos, permite + - ( ) espacios)");

        //Fila 5: Código postal
        txtCodigoPostal = new JTextField(22);
        aplicarFiltro(txtCodigoPostal, 4, filtroTexto.Tipo.SOLO_NUMERICO);
        agregarFila(panelCampos, gbc, 5, "Código Postal: *", txtCodigoPostal, "(4 dígitos numéricos)");

        //Fila 6: Domicilio
        txtDomicilio = new JTextField(22);
        aplicarFiltro(txtDomicilio, 50, filtroTexto.Tipo.CUALQUIERA);
        agregarFila(panelCampos, gbc, 6, "Domicilio: *", txtDomicilio, "(máx. 50 caracteres)");

        panelPrincipal.add(panelCampos, BorderLayout.CENTER);

        //panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));

        //fila superior: Validar + Limpiar
        JPanel filaSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton btnValidar = crearBoton("Validar", new Color(70, 130, 180));
        JButton btnLimpiar = crearBoton("Limpiar", new Color(150, 150, 150));
        filaSuperior.add(btnValidar);
        filaSuperior.add(btnLimpiar);

        //fila inferior: Cerrar
        JPanel filaInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton btnCerrar = crearBoton("Cerrar", new Color(200, 70, 70));
        filaInferior.add(btnCerrar);

        panelBotones.add(filaSuperior);
        panelBotones.add(filaInferior);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        //acciones de botones
        btnValidar.addActionListener(e -> validarFormulario());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnCerrar.addActionListener(e -> {
            int resp = JOptionPane.showConfirmDialog(this, "¿Cerrar la aplicación?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) System.exit(0);
        });
        add(panelPrincipal);
    }

    //agrega una fila: label | campo | hint
    private void agregarFila(JPanel panel, GridBagConstraints gbc, int fila, String labelTexto, JTextField campo, String hint) {
        //Columna 0: etiqueta
        gbc.gridx = 0; gbc.gridy = fila;
        gbc.weightx = 0;
        JLabel lbl = new JLabel(labelTexto);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(lbl, gbc);

        //Columna 1: campo
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(campo, gbc);

        //Columna 2: hint en gris
        gbc.gridx = 2; gbc.weightx = 0;
        JLabel lblHint = new JLabel(hint);
        lblHint.setFont(new Font("Arial", Font.ITALIC, 11));
        lblHint.setForeground(Color.GRAY);
        panel.add(lblHint, gbc);
    }

    //aplica un DocumentFilter al campo
    private void aplicarFiltro(JTextField campo, int max, filtroTexto.Tipo tipo) {
        AbstractDocument doc = (AbstractDocument) campo.getDocument();
        doc.setDocumentFilter(new filtroTexto(max, tipo));
    }

    //crea un botón estilizado
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 35));
        return btn;
    }

    //validación fuera de campo (al pulsar Validar)
    private void validarFormulario() {
        List<String> errores = new ArrayList<>();
        resetearColores();

        //nombre obligatorio
        if (txtNombre.getText().trim().isEmpty()) {
            errores.add("El campo Nombre es obligatorio.");
            txtNombre.setBackground(COLOR_ERROR);
        } else {
            txtNombre.setBackground(COLOR_OK);
        }

        //apellido obligatorio
        if (txtApellido.getText().trim().isEmpty()) {
            errores.add("El campo Apellido es obligatorio.");
            txtApellido.setBackground(COLOR_ERROR);
        } else {
            txtApellido.setBackground(COLOR_OK);
        }

        //DNI y pasaporte: no pueden estar vacios
        String dni = txtDNI.getText().trim();
        String pasaporte = txtPasaporte.getText().trim();

        if (dni.isEmpty() && pasaporte.isEmpty()) {
            errores.add("Debe ingresar DNI o Pasaporte (no puede dejar ambos vacíos).");
            txtDNI.setBackground(COLOR_ERROR);
            txtPasaporte.setBackground(COLOR_ERROR);
        } else if (!dni.isEmpty() && !pasaporte.isEmpty()) {
            errores.add("No puede ingresar DNI y Pasaporte al mismo tiempo. Solo uno.");
            txtDNI.setBackground(COLOR_ERROR);
            txtPasaporte.setBackground(COLOR_ERROR);
        } else {
            //validar el que tiene contenido
            if (!dni.isEmpty()) {
                String errDNI = validadorDNI.validarDNI(dni);
                if (errDNI != null) {
                    errores.add(errDNI);
                    txtDNI.setBackground(COLOR_ERROR);
                } else {
                    txtDNI.setBackground(COLOR_OK);
                }
            } else {
                String errPas = validadorDNI.validarPasaporte(pasaporte);
                if (errPas != null) {
                    errores.add(errPas);
                    txtPasaporte.setBackground(COLOR_ERROR);
                } else {
                    txtPasaporte.setBackground(COLOR_OK);
                }
            }
        }

        //teléfono
        String errTel = validadorDNI.validarTelefono(txtTelefono.getText().trim());
        if (errTel != null) {
            errores.add(errTel);
            txtTelefono.setBackground(COLOR_ERROR);
        } else {
            txtTelefono.setBackground(COLOR_OK);
        }

        //código postal
        String errCP = validadorDNI.validarCodigoPostal(txtCodigoPostal.getText().trim());
        if (errCP != null) {errores.add(errCP);
            txtCodigoPostal.setBackground(COLOR_ERROR);
        } else {
            txtCodigoPostal.setBackground(COLOR_OK);
        }

        //domicilio obligatorio
        if (txtDomicilio.getText().trim().isEmpty()) {errores.add("El campo Domicilio es obligatorio.");
            txtDomicilio.setBackground(COLOR_ERROR);
        } else {
            txtDomicilio.setBackground(COLOR_OK);
        }

        //mostrar resultado
        if (errores.isEmpty()) {JOptionPane.showMessageDialog(this, "✔ Formulario válido. Todos los campos son correctos.", "Validación exitosa", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder("Se encontraron los siguientes errores:\n\n");
            for (String err : errores) sb.append("• ").append(err).append("\n");
            JOptionPane.showMessageDialog(this, sb.toString(), "Errores de validación", JOptionPane.ERROR_MESSAGE);
        }
    }

    //verificación en tiempo real: DNI y pasaporte excluyentes
    private void verificarExclusividadDocumento() {
        boolean hayDNI       = !txtDNI.getText().trim().isEmpty();
        boolean hayPasaporte = !txtPasaporte.getText().trim().isEmpty();
        if (hayDNI && hayPasaporte) {
            txtDNI.setBackground(COLOR_ERROR);
            txtPasaporte.setBackground(COLOR_ERROR);
        } else {
            if (txtDNI.getBackground().equals(COLOR_ERROR))
                txtDNI.setBackground(COLOR_NORMAL);
            if (txtPasaporte.getBackground().equals(COLOR_ERROR))
                txtPasaporte.setBackground(COLOR_NORMAL);
        }
    }

    //limpia todos los campos y colores
    private void limpiarFormulario() {
        JTextField[] campos = {txtNombre, txtApellido, txtDNI, txtPasaporte, txtTelefono, txtCodigoPostal, txtDomicilio};
        for (JTextField c : campos) {
            c.setText("");
            c.setBackground(COLOR_NORMAL);
        }
        txtNombre.requestFocus();
    }

    private void resetearColores() {
        JTextField[] campos = {txtNombre, txtApellido, txtDNI, txtPasaporte, txtTelefono, txtCodigoPostal, txtDomicilio};
        for (JTextField c : campos) c.setBackground(COLOR_NORMAL);
    }

    //main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new formularioContacto().setVisible(true));
    }
}