package xmlreader.domain;

public class LibDiffsModel {
	String header, type, target, old_value, old_size, new_value, param_pos;

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getOld_value() {
		return old_value;
	}

	public void setOld_value(String old_value) {
		this.old_value = old_value;
	}

	public String getOld_size() {
		return old_size;
	}

	public void setOld_size(String old_size) {
		this.old_size = old_size;
	}

	public LibDiffsModel(String header, String type, String target, String old_value, String old_size, String new_value,
			String param_pos) {
		super();
		this.header = header;
		this.type = type;
		this.target = target;
		this.old_value = old_value;
		this.old_size = old_size;
		this.new_value = new_value;
		this.param_pos = param_pos;
	}

	public String getNew_value() {
		return new_value;
	}

	public void setNew_value(String new_value) {
		this.new_value = new_value;
	}

	public String getParam_pos() {
		return param_pos;
	}

	public void setParam_pos(String param_pos) {
		this.param_pos = param_pos;
	}
}
