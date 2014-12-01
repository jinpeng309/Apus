package apus.server

import akka.actor.{ActorRef, ActorSystem}
import apus.auth.UserAuth
import apus.protocol.Jid
import apus.util.UuidGenerator
import com.typesafe.config.Config
import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.util.SelfSignedCertificate

/**
 * Runtime context of current XMPP server.
 * Created by Hao Chen on 2014/11/17.
 */
trait ServerRuntime {

  val domain: String
  lazy val serverJid = Jid(domain, requireNode = false)

  val port: Int

  val sslContext: SslContext

  def nextSessionId(): String

  def actorSystem(): ActorSystem

  def router(): ActorRef

  def userAuth(): UserAuth
}

object ServerRuntime{

  def fromConfig(server: XmppServer, config: Config) = {
    new FromConfigServerRuntime(server, config)
  }
}

class FromConfigServerRuntime(server: XmppServer, config: Config) extends ServerRuntime{

  override val domain: String = config.getString("apus.server.domain")

  override val port: Int = config.getInt("apus.server.port")

  private val ssc = new SelfSignedCertificate()
  override val sslContext: SslContext = SslContext.newServerContext(ssc.certificate, ssc.privateKey)

  override def nextSessionId(): String = UuidGenerator.next()

  override def actorSystem(): ActorSystem = server.actorSystem

  override def router(): ActorRef = server.router

  override def userAuth(): UserAuth = server.userAuth

}