/*
 * Copyright (c) 2010, Oracle. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of Oracle nor the names of its contributors
 *   may be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package util;

import com.fasterxml.jackson.databind.ObjectMapper;

//import com.fasterxml.jackson.dataformat.xml.XmlMapper;
//import forseti.jpa.dossier.Partie;
//import forseti.util.pojo.PartieOutput;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.time.DateUtils;

/**
 *
 * @author mbohm
 */
public class JsfUtil {

    public static String LDAPURL, GROUPBASEDN, PEOPLEBASEDN, CURRENTTPICODE, IDTPICODE, RECIPIENTSMAIL, SMTPSERVER, SMTPUSER, SMTPPASSWORD, VERSION,currentYear;

    public static String getVERSION() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        VERSION = ctx.getExternalContext().getInitParameter("VERSION");
        return VERSION;
    }

    public static String getRECIPIENTSMAIL() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        RECIPIENTSMAIL = ctx.getExternalContext().getInitParameter("RECIPIENTSMAIL");
        return RECIPIENTSMAIL;
    }

    public static String getSMTPSERVER() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        SMTPSERVER = ctx.getExternalContext().getInitParameter("SMTPSERVER");
        return SMTPSERVER;
    }

    public static String getSMTPUSER() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        SMTPUSER = ctx.getExternalContext().getInitParameter("SMTPUSER");
        return SMTPUSER;
    }

    public static String getSMTPPASSWORD() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        SMTPPASSWORD = ctx.getExternalContext().getInitParameter("SMTPPASSWORD");
        return SMTPPASSWORD;
    }

    public static String getLDAPURL() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        LDAPURL = ctx.getExternalContext().getInitParameter("LDAPURL");
        return LDAPURL;
    }

    public static String getGROUPBASEDN() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        GROUPBASEDN = ctx.getExternalContext().getInitParameter("GROUPBASEDN");
        return GROUPBASEDN;
    }

    public static String getPEOPLEBASEDN() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        PEOPLEBASEDN = ctx.getExternalContext().getInitParameter("PEOPLEBASEDN");
        return PEOPLEBASEDN;
    }

    public static String getCURRENTTPICODE() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        CURRENTTPICODE = ctx.getExternalContext().getInitParameter("CURRENTTPICODE");
        return CURRENTTPICODE;
    }

    public static String getIDTPICODE() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        IDTPICODE = ctx.getExternalContext().getInitParameter("IDTPICODE");
        return IDTPICODE;
    }

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static void ensureAddErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, getBundleMsg("MsgTitleError"), msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));

        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("type",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "error", ""));
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundleMsg("MsgTitleInfo"), msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message",
                new FacesMessage(FacesMessage.SEVERITY_INFO, msg, ""));

        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("type",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "info", ""));
    }

    public static void addWarningMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, getBundleMsg("MsgTitleWarn"), msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addFatalMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_FATAL, getBundleMsg("MsgTitleFatal"), msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addFlashErrorMessage(String msg) {

        FacesContext facesContext;
        facesContext = FacesContext.getCurrentInstance();
        Flash flash;
        flash = facesContext.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        flash.setRedirect(true);
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        facesContext.addMessage(null, facesMsg);
    }

    public static void addFlashSuccessMessage(String msg) {
        FacesContext facesContext;
        facesContext = FacesContext.getCurrentInstance();
        Flash flash;
        flash = facesContext.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        flash.setRedirect(true);

        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        facesContext.addMessage(null, facesMsg);
    }

    public static void addFlashWarningMessage(String msg) {
        FacesContext facesContext;
        facesContext = FacesContext.getCurrentInstance();
        Flash flash;
        flash = facesContext.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        flash.setRedirect(true);

        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
        facesContext.addMessage(null, facesMsg);
    }

    public static void showSuccessMessage(String messageKey) {
        showSuccessMessage("MsgTitleInfo", messageKey);
    }

    public static void showSuccessMessage(String messageKey, Object... args) {
        showSuccessMessage("MsgTitleInfo", messageKey, args);
    }

    public static void showSuccessMessage(String titleKey, String messageKey) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundleMsg(titleKey), getBundleMsg(messageKey));
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void showSuccessMessage(String titleKey, String messageKey, Object... args) {
        String msg = getBundleMsg(messageKey);
        msg = String.format(msg, args);
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundleMsg(titleKey), msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void showErrorMessage(String messageKey) {
        showErrorMessage("MsgTitleInfo", messageKey);
    }

    public static void showErrorMessage(String titleKey, String messageKey) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, getBundleMsg(titleKey), getBundleMsg(messageKey));
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

    public static <T> Collection<T> arrayToCollection(T[] arr) {
        if (arr == null) {
            return new ArrayList<T>();
        }
        return Arrays.asList(arr);
    }

    public static Object[] collectionToArray(Collection<?> c) {
        if (c == null) {
            return new Object[0];
        }
        return c.toArray();
    }

    public static String getAsConvertedString(Object object, Converter converter) {
        return converter.getAsString(FacesContext.getCurrentInstance(), null, object);
    }

    public static String getAsString(Object object) {
        if (object instanceof Collection<?>) {
            Collection<?> collection = (Collection<?>) object;
            if (collection.isEmpty()) {
                return "(No Items)";
            }
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (Object item : collection) {
                if (i > 0) {
                    sb.append("<br />");
                }
                sb.append(item);
                i++;
            }
            return sb.toString();
        }
        return String.valueOf(object);
    }

    public static String convertDate(Date d, String format) {
        try {
            SimpleDateFormat affiche = new SimpleDateFormat(format, Locale.FRENCH);
            return affiche.format(d);
        } catch (NullPointerException e) {
            return "";
        }
    }

    public static Date convertDate(String d, String format) {
        try {
            d = d.trim();
            SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.FRENCH);
            return (Date) formatter.parseObject(d);
        } catch (Exception e) {
            return null;
        }
    }



    public static Date convertirDate(String dateString, String format) {
        Date date = null;
        try {
            SimpleDateFormat formatToString = new SimpleDateFormat(format);
            date = formatToString.parse(dateString);
        } catch (ParseException ex) {
            //Logger.getLogger(JsfUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    /**
     * Convertir une date java.util.Date en java.time.LocalDateTime.
     *
     * @param date Date java.util.Date à convertir.
     * @return
     */
    public static LocalDateTime convertirEnLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Convertir une date java.util.Date en java.time.LocalDate.
     *
     * @param date Date java.util.Date à convertir.
     * @return
     */
    public static LocalDate convertirEnLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Convertir une date java.time.LocalDateTime en java.util.Date.
     *
     * @param dateTime Date java.time.LocalDateTime à convertir.
     * @return
     */
    public static Date convertirEnDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Returns a Date of the day at the time of midnight, 00:00, at the start of
     * this date.
     *
     * @param date
     * @return
     */
    public static Date getStartOfDay(Date date) {
        LocalDateTime ldt = JsfUtil.convertirEnLocalDate(date).atStartOfDay();
        return JsfUtil.convertirEnDate(ldt);
    }

    /**
     * Returns a Date set to the last possible millisecond of the day, just
     * before midnight.
     *
     * @param date
     * @return
     */
    public static Date getEndOfDay(Date date) {
        LocalDateTime ldt = JsfUtil.convertirEnLocalDate(date).atStartOfDay().plusDays(1).minusNanos(1);
        return JsfUtil.convertirEnDate(ldt);
    }

    public static Long getDaysBetween(Date debut, Date fin) {
        LocalDate ldebut = convertirEnLocalDate(debut);
        LocalDate lfin = convertirEnLocalDate(fin);
        return ChronoUnit.DAYS.between(ldebut, lfin);
    }

//    /**
//     * Returns a Date set to the last possible millisecond of the day, just
//     * before midnight. If a null day is passed in, a new Date is created.
//     * midnight (00m 00h 00s)
//     *
//     * @param day
//     * @return
//     */
//    public static Date getEndOfDay(Date day) {
//        if (day == null) {
//            return new Date();
//        } else {
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(day);
//            cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
//            cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
//            cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
//            cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
//            return cal.getTime();
//        }
//    }
    public static String getBundleMsg(String key) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle("util.Bundle", ctx.getViewRoot().getLocale());
        return bundle.containsKey(key) ? bundle.getString(key) : ("?" + key + "?");
    }

    public static String getFileName(String extension) {
        String retour = new SimpleDateFormat("ddMMyyyyHHmmSSsss", Locale.FRENCH).format(new Date());
        if (!((extension == null) || extension.equals(""))) {
            retour += "." + extension.toUpperCase();
        }
        return retour;
    }

    public static String getFileName(String extension, String prefixe) {
        String retour = new SimpleDateFormat("ddMMyyyyHHmmSSsss", Locale.FRENCH).format(new Date());
        String pre = prefixe;
        pre = pre.replaceAll(" ", "_");
        pre = sansAccent(pre);
        pre = pre.toUpperCase();
        if (!((extension == null) || extension.equals(""))) {
            retour += "." + extension.toUpperCase();
        }
        retour = pre + "_" + retour;

        System.out.println("name est: " + retour);
        return retour;
    }

    public static String getFileNameWithDate(String extension, String prefixe) {
        Date myDate = new Date();
        String retour = convertDate(myDate, "EEEEE dd MMM yyyy 'A' HH:mm:ss");
       // SimpleDateFormat myDate = new SimpleDateFormat("EEEEE dd MMM yyyy 'A' HH:mm:ss");

        //String retour = new SimpleDateFormat("ddMMyyyyHHmmSSsss", Locale.FRENCH).format(new Date());
        //String retour = ""+ myDate;
        String pre = prefixe;
        pre = pre.replaceAll(" ", "_");
        pre = sansAccent(pre);
        pre = pre.toUpperCase();
        if (!((extension == null) || extension.equals(""))) {
            retour += "." + extension.toUpperCase();
        }
        retour = pre + "_" + retour;

        System.out.println("name est: " + retour);
        return retour;
    }

    public static String sansAccent(String chaine) {
        return Normalizer.normalize(chaine, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
    }

    /**
     * Convertit l'objet spécifiée en chaîne de caractères JSON.
     *
     * @author Raman
     * @param object Objet à convertir en JSON.
     * @return Représentation en notation JSON de l'objet.
     */
    public static String toJson(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    /**
     * Convertit chaîne de caractères JSON en un objet de type spécifié.
     *
     * @author Raman
     * @param <T> Type de l'objet.
     * @param json Chaîne de caractères JSON.
     * @param type Class du type de l'objet
     * @return Objet obtenu.
     */
    public static <T> T fromJson(String json, Class<T> type) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, type);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

//    /**
//     * Convertit l'objet spécifiée en chaîne de caractères XML.
//     *
//     * @author Raman
//     * @param object Objet à convertir en JSON.
//     * @return Représentation en notation JSON de l'objet.
//     */
//    public static String toXml(Object object) {
//        try {
//            XmlMapper mapper = new XmlMapper();
//            return mapper.writeValueAsString(object);
//        } catch (IOException ex) {
//            System.out.println(ex.getMessage());
//            return null;
//        }
//    }
//    /**
//     * Convertit chaîne de caractères XML en un objet de type spécifié.
//     *
//     * @author Raman
//     * @param <T> Type de l'objet.
//     * @param xml Chaîne de caractères JSON.
//     * @param type Class du type de l'objet
//     * @return Objet obtenu.
//     */
//    public static <T> T fromXml(String xml, Class<T> type) {
//        try {
//            XmlMapper mapper = new XmlMapper();
//            return mapper.readValue(xml, type);
//        } catch (IOException ex) {
//            System.out.println(ex.getMessage());
//            return null;
//        }
//    }
   

    public static void sendFile(ByteArrayOutputStream out, String fileName) throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        try {
            response.getOutputStream().write(out.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();
            facesContext.responseComplete();
            facesContext.renderResponse();
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        } finally {
        }

    }

    public static void sendFileUtf8(ByteArrayOutputStream out, String fileName) throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document;charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        try {
            response.getOutputStream().write(out.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();
            facesContext.responseComplete();
            facesContext.renderResponse();
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        } finally {
        }

    }

    public static String encoderBase64(String str) throws UnsupportedEncodingException {
        Base64.Encoder encodeur = Base64.getEncoder();
        byte[] raw = str.getBytes("UTF-8");
        return encodeur.encodeToString(raw);
    }

    public static String decoderBase64(String base64) throws UnsupportedEncodingException {
        Base64.Decoder decodeur = Base64.getDecoder();
        byte[] raw = decodeur.decode(base64);
        return new String(raw, "UTF-8");
    }

    public static boolean sameDate(Date d1, Date d2) {
        return DateUtils.isSameDay(d1, d2);
    }

    /**
     * Journalise les messages d'info.
     *
     * @author ram,
     * @param logger
     * @param msg
     */
    public static void logInfo(org.apache.log4j.Logger logger, Object msg) {
        System.out.println(msg);
        logger.info(msg);
    }

    /**
     * Journalise les messages d'erreur.
     *
     * @author ram,
     * @param logger
     * @param msg
     */
    public static void logError(org.apache.log4j.Logger logger, Object msg) {
        System.err.println(msg);
        logger.error(msg);
    }

    public static Date convertLongDate(String dt) {
        return convertDate(dt, "EEEEE dd MMMM yyyy HH:mm");
    }
 public static String convertDate(Date d) {
        return convertDate(d, "EEEEE dd MMMM yyyy");
    }
 public static String convertDateHeure(Date d) {
        return convertDate(d, "EEEEE dd MMMM yyyy HH:mm");
    }
    public Date dateTime(Date date, Date time) {
        return new Date(
                date.getYear(), date.getMonth(), date.getDay(),
                time.getHours(), time.getMinutes(), time.getSeconds()
        );
    }
 public static Long getDureeEnJours(Date debut, Date fin) {
        return JsfUtil.getDaysBetween(debut, fin);
//        LocalDate ldebut = JsfUtil.convertirEnLocalDate(debut);
//        LocalDate lfin = JsfUtil.convertirEnLocalDate(fin);
//        return ChronoUnit.DAYS.between(ldebut, lfin);
    }
  public static String convertFileSize(long size) {
        return FileSizeFormater.format(size);
    }
  public static String getCurrentYear() {
     currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
      return currentYear;
    }
  
}
