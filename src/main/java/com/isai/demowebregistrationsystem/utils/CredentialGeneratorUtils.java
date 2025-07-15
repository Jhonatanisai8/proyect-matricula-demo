package com.isai.demowebregistrationsystem.utils;

import java.security.SecureRandom;
import java.util.Random;

public class CredentialGeneratorUtils {

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String DIGIT = "0123456789";
    private static final String OTHER_CHAR = "!@#$%&*()_-+=";
    private static final String PASSWORD_CHARS = CHAR_LOWER + CHAR_UPPER + DIGIT + OTHER_CHAR;
    private static final int PASSWORD_LENGTH = 12;

    private static SecureRandom random = new SecureRandom();

    /**
     * Genera un nombre de usuario basado en las primeras 4 letras del nombre,
     * las primeras 4 letras del apellido y 3 dígitos aleatorios.
     * El nombre de usuario se convierte a minúsculas.
     *
     * @param nombres   Los nombres de la persona.
     * @param apellidos Los apellidos de la persona.
     * @return Un nombre de usuario generado.
     */
    public static String generateUsername(String nombres, String apellidos) {
        String userPart = "";
        if (nombres != null && nombres.length() >= 4) {
            userPart += nombres.substring(0, 4);
        } else if (nombres != null) {
            userPart += nombres;
        }

        if (apellidos != null && apellidos.length() >= 4) {
            userPart += apellidos.substring(0, 4);
        } else if (apellidos != null) {
            userPart += apellidos;
        }

        Random randomNum = new Random();
        int randomNumber = randomNum.nextInt(900) + 100;

        return (userPart.toLowerCase() + randomNumber);
    }

    /**
     * Genera una contraseña aleatoria y segura.
     * Asegura que la contraseña contenga al menos una mayúscula, una minúscula, un dígito y un carácter especial.
     *
     * @return Una contraseña generada aleatoriamente.
     */
    public static String generateRandomPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        // Asegurar al menos un carácter de cada tipo
        password.append(CHAR_LOWER.charAt(random.nextInt(CHAR_LOWER.length())));
        password.append(CHAR_UPPER.charAt(random.nextInt(CHAR_UPPER.length())));
        password.append(DIGIT.charAt(random.nextInt(DIGIT.length())));
        password.append(OTHER_CHAR.charAt(random.nextInt(OTHER_CHAR.length())));

        // Rellenar el resto de la contraseña con caracteres aleatorios
        for (int i = 4; i < PASSWORD_LENGTH; i++) {
            password.append(PASSWORD_CHARS.charAt(random.nextInt(PASSWORD_CHARS.length())));
        }

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomIndex = random.nextInt(PASSWORD_LENGTH);
            char temp = password.charAt(i);
            password.setCharAt(i, password.charAt(randomIndex));
            password.setCharAt(randomIndex, temp);
        }

        return password.toString();
    }
}