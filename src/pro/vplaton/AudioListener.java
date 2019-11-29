package pro.vplaton;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import javax.sound.sampled.LineEvent.Type;

public class AudioListener implements LineListener {
    // Слушатель окончания звука,
    // без него прога завершится сразу
    // после начала проигрывания. (clip.start();)
    // (не завершится если дальше будет код).
    private boolean done = false;
    @Override public synchronized void update(LineEvent event) {
        Type eventType = event.getType();
        if (eventType == Type.STOP || eventType == Type.CLOSE) {
            done = true;
            notifyAll();
        }
    }

    public synchronized void waitUntilDone() throws InterruptedException {
        while (!done) { wait();}
    }

    public void soundPlay() throws URISyntaxException {
        try {
            // Чтение при помощи потоков, прямо из jar
            // (+ слеш т.к без него скомпилированная прога не работает.)
            InputStream bufferedIn1 = new BufferedInputStream(Main.class.getResourceAsStream("sounds/manwah.wav"));

            AudioListener listener = new AudioListener();

            AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn1);
            if( !AudioSystem.isLineSupported(new DataLine.Info(Clip.class, ais.getFormat())) ) System.exit(-1); // Всё в одной строке

            Clip clip = AudioSystem.getClip();
            clip.addLineListener(listener); // Добавление слушателя.
            clip.open(ais);
            clip.start();
            listener.waitUntilDone(); // Пауза до окончания клипа (звука).

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка воспроизведения звука! Надеюсь это не напечатается :)");
        }
    }
}

