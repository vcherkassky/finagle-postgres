package com.twitter.finagle.postgres.protocol

import org.jboss.netty.buffer.ChannelBuffer
import java.nio.charset.Charset
import scala.StringBuilder
import java.security.MessageDigest
import scala.annotation.tailrec

object Charsets {

  val Utf8 = Charset.forName("UTF-8")

}

object Buffers {

  /**
   * Reads a string with C-style '\0' terminator at the end from a buffer
   */
  @throws(classOf[IndexOutOfBoundsException])
  def readCString(buffer: ChannelBuffer): String = {
    @tailrec
    def countChars(buf: ChannelBuffer, count: Int): Int = {
      if(!buffer.readable) throw new IndexOutOfBoundsException("buffer ended, but '\0' was not found")
      else if (buffer.readByte() == 0)
        count
      else
        countChars(buf, count + 1)
    }

    buffer.markReaderIndex()
    // search for '\0'
    val count = countChars(buffer, 0)
    buffer.resetReaderIndex()

    // read a string without '\0'
    val result = buffer.toString(buffer.readerIndex(), count, Charsets.Utf8)
    // set reader index to the whole string length - including '\0'
    buffer.readerIndex(buffer.readerIndex() + count + 1)
    result
  }

}

object Md5Encryptor {

  @throws(classOf[IllegalArgumentException])
  def encrypt(user: Array[Byte], password: Array[Byte], salt: Array[Byte]): Array[Byte] = {

    require(user != null && user.length > 0, "user should not be empty")
    require(password != null && password.length > 0, "password should not be empty")
    require(salt != null && salt.length > 0, "salt should not be empty")
    
    val inner = MessageDigest.getInstance("MD5")
    inner.update(password)
    inner.update(user)

    val outer = MessageDigest.getInstance("MD5")
    outer.update(Hex.valueOf(inner.digest).getBytes)
    outer.update(salt)

    ("md5" + Hex.valueOf(outer.digest)).getBytes
  }

}

object Hex {
  def valueOf(buf: Array[Byte]): String = buf.map("%02X" format _).mkString.toLowerCase
}
