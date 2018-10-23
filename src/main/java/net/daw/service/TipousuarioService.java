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
	//Hola
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
					//http://localhost:8080/trolleyes/json?op=get&ob=tipousuario&id=1
					break;
				case "delete":
					oTipousuarioDao = new TipousuarioDao(oConnection);
					oTipousuarioBean = oTipousuarioDao.delete(id);
					//http://localhost:8080/trolleyes/json?op=delete&ob=tipousuario&id=2
					break;
				case "count":
					oTipousuarioDao = new TipousuarioDao(oConnection);
					oTipousuarioBean = oTipousuarioDao.getCount();
					//http://localhost:8080/trolleyes/json?op=count&ob=tipousuario&id=1
					break;
				case "update": 
					oTipousuarioDao = new TipousuarioDao(oConnection);
					oTipousuarioBean = oTipousuarioDao.update(desc, id);
					//http://localhost:8080/trolleyes/json?op=update&ob=tipousuario&desc=Holabebes&id=1
					break;
				case "create":
					oTipousuarioDao = new TipousuarioDao(oConnection);
					oTipousuarioBean = oTipousuarioDao.create(desc);
					//http://localhost:8080/trolleyes/json?op=create&ob=tipousuario&desc=NuevoUsuario
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
