package loopdospru.lvip.corja;

import com.google.gson.annotations.Expose;
import loopdospru.lvip.config.General;

import java.util.List;

public class VIP {

    @Expose
    private String nome;

    @Expose
    private String prefixo;

    @Expose
    private boolean ativo;

    @Expose
    private String data;

    @Expose
    private String tempo;

    @Expose
    private List<String> comandos;

    public VIP(String nome, String prefixo, boolean ativo, String data, String tempo, List<String> comandos) {
        this.nome = nome;
        this.prefixo = prefixo;
        this.ativo = ativo;
        this.data = data;
        this.tempo = tempo;
        this.comandos = comandos;
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

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public List<String> getComandos() {
        return comandos;
    }

    public void setComandos(List<String> comandos) {
        this.comandos = comandos;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
