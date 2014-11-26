package apus.protocol

import scala.xml.Elem

/**
 * Created by Hao Chen on 2014/11/24.
 */
class Iq(override val xml: Elem) extends Stanza{

  import Iq._

  require(verify(xml), "This xml is not a valid Iq stanza")

  override def label: String = Iq.Label

  val id = idOpt.get

  val iqType = IqType(attr("type"))

}

object Iq {

  import apus.util.XmlUtil._

  val Label: String = "iq"

  /**
   * verify whether this xml is a valid IQ stanza
   * @param xml
   * @return
   */
  def verify(xml: Elem): Boolean = {
    xml.label == Label &&
      attr(xml, "id").isDefined
  }

}

object IqType extends Enumeration{

  val Get = Value("get")
  val Set = Value("set")
  val Result = Value("result")
  val Error = Value("error")
  val Unknown = Value("unknown")

  def apply(str: String): Value = values.find( _.toString == str.toLowerCase ).getOrElse(Unknown)

  def apply(strOpt: Option[String]): Value = strOpt.map(apply(_)).get
}