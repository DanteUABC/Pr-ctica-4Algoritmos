package eightoff;

public class NodoDoble<T>
{
    private NodoDoble ant, sig;
    private T info;
    
    public NodoDoble()
    {
        ant = null;
        sig = null;
        info = null;
    }
    
    public NodoDoble(T info)
    {
        ant = null;
        sig = null;
        this.info = info;
    }
    
    public NodoDoble getAnt()
    {
        return ant;
    }
    
    public NodoDoble getSig()
    {
        return sig;
    }
    
    public void setAnt(NodoDoble ant)
    {
        this.ant = ant;
    }
    
    public void setSig(NodoDoble sig)
    {
        this.sig = sig;
    }
    
    public T getInfo()
    {
        return info;
    }
    
    public void setInfo(T info)
    {
        this.info = info;
    }
}
