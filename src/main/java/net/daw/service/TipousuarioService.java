package net.daw.service;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

import net.daw.bean.ReplyBean;
import net.daw.bean.TipousuarioBean;
import net.daw.connection.publicinterface.ConnectionInterface;
import net.daw.constant.ConnectionConstants;
import net.daw.dao.TipousuarioDao;
import net.daw.factory.ConnectionFactory;
import net.daw.helper.EncodingHelper;

public class TipousuarioService {

	HttpServletRequest oRequest;

	public TipousuarioService(HttpServletRequest oRequest) {
		super();
		this.oRequest = oRequest;
	}

	public ReplyBean get() throws Exception {
		ReplyBean oReplyBean;
		ConnectionInterface oConnectionPool = null;
		Connection oConnection;
		TipousuarioDao oTipousuarioDao;
		TipousuarioBean oTipousuarioBean = null;
		try {
			Integer id = Integer.parseInt(oRequest.getParameter("id"));
			String desc = oRequest.getParameter("desc");
			// Buscamos que tipo de conexion necesitamos.
			oConnectionPool = ConnectionFactory.getConnection(ConnectionConstants.connectionPool);
			// Ya tenemos la conexion con Hikari
			oConnection = oConnectionPool.newConnection();
			switch (oRequest.getParameter("op")) {
				case "get":
					oTipousuarioDao = new TipousuarioDao(oConnection);
					oTipousuarioBean = oTipousuarioDao.get(id);
					break;
				case "delete":
					oTipousuarioDao = new TipousuarioDao(oConnection);
					oTipousuarioBean = oTipousuarioDao.delete(id);
					break;
				case "getCount":
					oTipousuarioDao = new TipousuarioDao(oConnection);
					oTipousuarioBean = oTipousuarioDao.getCount();
					break;
				case "update":
					oTipousuarioDao = new TipousuarioDao(oConnection);
					oTipousuarioBean = oTipousuarioDao.update(desc, id);
					break;
				case "create":
					oTipousuarioDao = new TipousuarioDao(oConnection);
					oTipousuarioBean = oTipousuarioDao.create(desc);
					break;
			}

			Gson oGson = new Gson();
			oReplyBean = new ReplyBean(200, oGson.toJson(oTipousuarioBean));
		} catch (Exception ex) {
			oReplyBean = new ReplyBean(500,
					"Bad Connection: " + EncodingHelper.escapeQuotes(EncodingHelper.escapeLine(ex.getMessage())));
		} finally {
			oConnectionPool.disposeConnection();
		}

		return oReplyBean;

	}

}
