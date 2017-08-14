package greencar77.jump.generator;

public class CodeManager {
    public static StringBuilder code(String... lines) {
        StringBuilder result = new StringBuilder();

        for (String line: lines) {
            result.append(line + Generator.LF);
        }

        return result;
    }
    
    public static String[] indent(String offset, String... lines) {
        for (int i = 0; i < lines.length; i++) {
            if (!lines[i].equals(Generator.LF)) {
                lines[i] = offset + lines[i];
            }
        }
        return lines;
    }
}
