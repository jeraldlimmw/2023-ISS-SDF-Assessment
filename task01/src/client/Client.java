package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {

    public static void main (String[] args) {

        try {
            Socket sock = new Socket(args[0], Integer.parseInt(args[1]));

            InputStream is = sock.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);

            OutputStream os = sock.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);

            String input = ois.readUTF();

            // convert String into an array of number Strings
            String[] numStrArr = input.trim().split("[,]");
            // convert number String to double and add to number list
            List<Double> numList = new ArrayList<>();
            for (String s : numStrArr) {
                numList.add(Double.parseDouble(s));
            }

            // output
            oos.writeUTF("Lim Ming Wei, Jerald");
            oos.writeUTF("jerald.mw.lim@gmail.com");
            oos.writeFloat((float) mean(numList));
            oos.writeFloat((float) stdDev(numList));
            oos.flush();

            oos.close();
            os.close();
            ois.close();
            is.close();
            sock.close();

        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    // take list of numbers and find sum
    public static double sum(List<Double> numbers) {
        double sum = 0.0;
        for (double x : numbers) {
            sum += x;
        }
        return sum;
    }

    // take list of numbers and find mean
    public static double mean(List<Double> numbers) {
        return (sum(numbers) / numbers.size());
    }

    // take list of numbers and find SD
    public static double stdDev(List<Double> numbers) {
        double mean = mean(numbers);
        double numerator = 0.0;
        // numerator = sum of (x - mean)^2
        for (double x : numbers) {
            numerator += Math.pow((x - mean), 2.0);
        }
        return Math.sqrt(numerator / numbers.size());
    }

}