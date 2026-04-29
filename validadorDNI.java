package formulario;

public class validadorDNI {

    //DNI: 8 dígitos, entre 10.000.000 y 60.000.000
    public static String validarDNI(String dni) {
        if (dni.isEmpty()) return null; //campo vacío es OK si hay pasaporte
        if (!dni.matches("\\d{8}")) {
            return "El DNI debe tener exactamente 8 dígitos numéricos.";
        }
        long numero = Long.parseLong(dni);
        if (numero < 10_000_000 || numero > 60_000_000) {
            return "El DNI debe estar entre 10.000.000 y 60.000.000.";
        }
        return null;
    }

    //pasaporte: 1 letra A-Z + 8 dígitos, parte numérica entre 10.000.000 y 60.000.000
    public static String validarPasaporte(String pasaporte) {
        if (pasaporte.isEmpty()) return null;
        if (!pasaporte.matches("[A-Za-z]\\d{8}")) {
            return "El pasaporte debe tener 1 letra seguida de 8 dígitos. Ej: N39392288";
        }
        long numero = Long.parseLong(pasaporte.substring(1));
        if (numero < 10_000_000 || numero > 60_000_000) {
            return "La parte numérica del pasaporte debe estar entre 10.000.000 y 60.000.000.";
        }
        return null;
    }

    //teléfono: mas de 6 digitos
    public static String validarTelefono(String telefono) {
        if (telefono.isEmpty()) return "El teléfono es obligatorio.";
        long digitCount = telefono.chars()
                .filter(Character::isDigit)
                .count();
        if (digitCount <= 6) {
            return "El teléfono debe tener más de 6 dígitos numéricos.";
        }
        return null;
    }

    //código postal: exactamente 4 dígitos
    public static String validarCodigoPostal(String cp) {
        if (cp.isEmpty()) return "El código postal es obligatorio.";
        if (!cp.matches("\\d{4}")) {
            return "El código postal debe tener exactamente 4 dígitos.";
        }
        return null;
    }
}