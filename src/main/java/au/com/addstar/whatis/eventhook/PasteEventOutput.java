package au.com.addstar.whatis.eventhook;

import org.kitteh.pastegg.PasteContent;

import java.io.StringWriter;
import java.io.Writer;

/**
 * Created for use for the Add5tar MC Minecraft server
 * Created by benjamincharlton on 12/05/2020.
 */
public class PasteEventOutput implements EventOutput {

	final StringWriter writer = new StringWriter();
	final PasteContent.ContentType format;

	public PasteEventOutput() {
		this.format = PasteContent.ContentType.TEXT;
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public Writer getWriter() {
		return writer;
	}

	public PasteContent getContent() {
		return new PasteContent(format, writer.toString());
	}
}
