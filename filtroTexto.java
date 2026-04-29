package formulario;

import javax.swing.text.*;

public class filtroTexto extends DocumentFilter {

    public enum Tipo {
        SOLO_ALFABETICO,
        SOLO_NUMERICO,
        ALFANUMERICO_PASAPORTE,
        TELEFONO,
        CUALQUIERA
    }

    private final int maxCaracteres;
    private final Tipo tipo;

    public filtroTexto(int maxCaracteres, Tipo tipo) {
        this.maxCaracteres = maxCaracteres;
        this.tipo = tipo;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
        if (text == null) return;
        String filtrado = filtrar(text, fb.getDocument().getLength());
        if (!filtrado.isEmpty()) {
            super.insertString(fb, offset, filtrado, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        if (text == null) return;
        int longitudActual = fb.getDocument().getLength() - length;
        String filtrado = filtrar(text, longitudActual);
        if (!filtrado.isEmpty() || length > 0) {
            super.replace(fb, offset, length, filtrado, attrs);
        }
    }

    private String filtrar(String texto, int longitudActual) {StringBuilder sb = new StringBuilder();
        for (char c : texto.toCharArray()) {
            if (longitudActual + sb.length() >= maxCaracteres) break;
            if (esValido(c)) sb.append(c);
        }
        return sb.toString();
    }

    private boolean esValido(char c) {
        return switch (tipo) {
            case SOLO_ALFABETICO -> Character.isLetter(c) || c == ' ';
            case SOLO_NUMERICO -> Character.isDigit(c);
            case ALFANUMERICO_PASAPORTE -> Character.isLetterOrDigit(c);
            case TELEFONO -> Character.isDigit(c)
                    || "+-() ".indexOf(c) >= 0;
            case CUALQUIERA -> true;
        };
    }
}