import java.io.Serializable;


public class Message implements Serializable {

	private static final long serialVersionUID = -2723363051271966964L;
	private Integer x=null,y=null,result=null;

	public Message(Integer x, Integer y) {
		this.x=x;
		this.y=y;
	}
	public Integer getX() {
		return x;
	}
	public Integer getY() {
		return y;
	}
	public Integer getResult() {
		return result;
	}
	public void setX(Integer x) {
		this.x=x;
	}
	public void setY(Integer y) {
		this.y=y;
	}
	public void setResult(Integer result) {
		this.result=result;
	}
}
