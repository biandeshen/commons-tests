package xyz.biandeshen.代码;

import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class PGPFileProcessor
{

    private String passphrase;
    private String publicKeyFileName;
    private InputStream secretKeyFileName;
    private String inputFileName;
    private String outputFileName;
    private boolean asciiArmored = false;
    private boolean integrityCheck = true;

    public boolean encrypt() throws Exception {
        FileInputStream keyIn = new FileInputStream(publicKeyFileName);
        FileOutputStream out = new FileOutputStream(outputFileName);
        PGPUtils.encryptFile(out, inputFileName, PGPUtils.readPublicKey(keyIn), asciiArmored, integrityCheck);
        out.close();
        keyIn.close();
        return true;
    }

    public boolean signEncrypt() throws Exception {
        FileOutputStream out = new FileOutputStream(outputFileName);
        FileInputStream publicKeyIn = new FileInputStream(publicKeyFileName);
        //FileInputStream secretKeyIn = new FileInputStream(secretKeyFileName);

        PGPPublicKey publicKey = PGPUtils.readPublicKey(publicKeyIn);
        PGPSecretKey secretKey = PGPUtils.readSecretKey(secretKeyFileName);

        PGPUtils.signEncryptFile(
                out,
                this.getInputFileName(),
                publicKey,
                secretKey,
                this.getPassphrase(),
                this.isAsciiArmored(),
                this.isIntegrityCheck() );

        out.close();
        publicKeyIn.close();
        secretKeyFileName.close();

        return true;
    }

    public boolean decrypt() throws Exception {
        FileInputStream in = new FileInputStream(inputFileName);
        //FileInputStream keyIn = new FileInputStream(secretKeyFileName);
        //BufferedWriter
        PGPUtils.decryptFile(in, outputFileName, secretKeyFileName, passphrase.toCharArray());
        in.close();
        secretKeyFileName.close();
        return true;
    }

    public boolean isAsciiArmored() {
            return asciiArmored;
    }

    public void setAsciiArmored(boolean asciiArmored) {
            this.asciiArmored = asciiArmored;
    }

    public boolean isIntegrityCheck() {
            return integrityCheck;
    }

    public void setIntegrityCheck(boolean integrityCheck) {
            this.integrityCheck = integrityCheck;
    }

    public String getPassphrase() {
            return passphrase;
    }

    public void setPassphrase(String passphrase) {
            this.passphrase = passphrase;
    }

    public String getPublicKeyFileName() {
            return publicKeyFileName;
    }

    public void setPublicKeyFileName(String publicKeyFileName) {
            this.publicKeyFileName = publicKeyFileName;
    }

    public InputStream getSecretKeyFileName() {
            return secretKeyFileName;
    }

    public void setSecretKeyFileName(InputStream secretKeyFileName) {
            this.secretKeyFileName = secretKeyFileName;
    }

    public String getInputFileName() {
            return inputFileName;
    }

    public void setInputFileName(String inputFileName) {
            this.inputFileName = inputFileName;
    }

    public String getOutputFileName() {
            return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
            this.outputFileName = outputFileName;
    }

}