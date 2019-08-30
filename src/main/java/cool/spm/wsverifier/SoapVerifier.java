package cool.spm.wsverifier;

import cool.spm.wsverifier.model.SoapAttachment;
import lombok.Data;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPMessage;
import java.io.File;
import java.io.InputStream;
import java.util.List;

public class SoapVerifier
{
  private SoapEngine engine;

  private String endpointAddress;

  private Resource requestFile;

  private List<SoapAttachment> attachments;

  private SOAPMessage message;

  public static SoapVerifier create()
  {
    return new SoapVerifier();
  }

  public SoapVerifier withEndpointAddress(String endpointAddress)
  {
    this.endpointAddress = endpointAddress;
    return this;
  }

  public SoapVerifier withRequestFile(File file)
  {
    return withRequest(new FileSystemResource(file));
  }

  public SoapVerifier withRequestInputStream(InputStream inputStream)
  {
    return withRequest(new InputStreamResource(inputStream));
  }

  public SoapVerifier withAttachment(String name, Resource attachment)
  {
    return null;
  }
  public SoapVerifier withRequest(Resource resource)
  {
    try
    {
      message = MessageFactory.newInstance().createMessage(new MimeHeaders(), resource.getInputStream());
      return this;
    }
    catch (Exception e)
    {
      throw new SoapVerifierException(e);
    }
  }

  public SoapEngine build()
  {

    return new SoapEngine(endpointAddress, requestFile, attachments, message);
  }

  @Data
  private static class SoapEngine
  {
    private final String endpointAddress;

    private final Resource requestFile;

    private final List<SoapAttachment> attachments;

    private SoapEngine(String endpointAddress, Resource requestFile, List<SoapAttachment> attachments, SOAPMessage message)
    {
      this.endpointAddress = endpointAddress;
      this.requestFile = requestFile;
      this.attachments = attachments;
    }
  }
}
