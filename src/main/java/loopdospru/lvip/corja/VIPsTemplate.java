package loopdospru.lvip.corja;

import com.google.gson.annotations.Expose;

import java.util.List;

public class VIPsTemplate {

    private String nome;
    private String prefixo;

    private String tempo;

    private List<String> comandos;

    private List<String> comandos_expirar;

    public VIPsTemplate(String nome,
                        String prefixo,
                        String tempo,
                        List<String> comandos) {
        this.nome = nome;
        this.prefixo = prefixo;
        this.tempo = tempo;
        this.comandos = comandos;
        this.comandos_expirar = comandos_expirar;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPrefixo() {
        return prefixo;
    }

    public void setPrefixo(String prefixo) {
        this.prefixo = prefixo;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public List<String> getComandos() {
        return comandos;
    }

    public void setComandos(List<String> comandos) {
        this.comandos = comandos;
    }
}
