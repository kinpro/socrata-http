package com.socrata.http.server.util.handlers

import com.socrata.http.server.HttpService
import javax.servlet.http.{HttpServletResponseWrapper, HttpServletResponse, HttpServletRequest}
import org.slf4j.{LoggerFactory, Logger}

class LoggingHandler(underlying: HttpService, log: Logger = LoggingHandler.defaultLog) extends HttpService {
  def apply(req: HttpServletRequest) = { resp =>
    val start = System.nanoTime()

    if(log.isInfoEnabled) {
      val reqStr = req.getMethod + " " + req.getRequestURI + Option(req.getQueryString).fold("") { q =>
        "?" + q
      }
      log.info(">>> " + reqStr)
    }
    class InspectableHttpServletResponse(underlying: HttpServletResponse) extends HttpServletResponseWrapper(underlying) {
      var status = 200
      override def setStatus(x: Int) {
        super.setStatus(x)
        status = x
      }
      override def setStatus(x: Int, m: String) {
        super.setStatus(x, m)
        status = x
      }
      override def sendError(x: Int) {
        super.sendError(x)
        status = x
      }
      override def sendError(x: Int, m: String) {
        super.sendError(x, m)
        status = x
      }
    }
    val trueResp = new InspectableHttpServletResponse(resp)
    try {
      underlying(req)(trueResp)
    } finally {
      val end = System.nanoTime()
      val extra =
        if(trueResp.status >= 400) " ERROR " + trueResp.status
        else ""
      log.info("<<< {}ms{}", (end - start)/1000000, extra)
    }
  }
}

object LoggingHandler {
  private val defaultLog = LoggerFactory.getLogger(classOf[LoggingHandler])
  def apply(service: HttpService, log: Logger = defaultLog): LoggingHandler = new LoggingHandler(service)
}
