package in.hiresense.servlets;

import java.io.IOException;

import in.hiresense.dao.ApplicationDAO;
import in.hiresense.dao.JobDAO;
import in.hiresense.dao.UserDAO;
import in.hiresense.models.JobPojo;
import in.hiresense.models.UserPojo;
import in.hiresense.utils.MailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/UpdateApplicationStatusServlet")
public class UpdateApplicationStatusServlet extends HttpServlet {
    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null ||
            !"employer".equals(session.getAttribute("userRole"))) {
            res.sendRedirect("login.jsp");
            return;
        }

        try {
            int appId = Integer.parseInt(req.getParameter("appId"));
            String newStatus = req.getParameter("status");
            int jobId = Integer.parseInt(req.getParameter("jobId"));

            if (!newStatus.equals("shortlisted") && !newStatus.equals("rejected")) {
                res.sendRedirect("ViewApplicantsServlet?jobId=" + jobId + "&error=invalid_status");
                return;
            }

            boolean updated = ApplicationDAO.updateApplicationStatus(appId, newStatus);
            if (updated) {
            	UserPojo user = UserDAO.getUserById(Integer.parseInt(session.getAttribute("userId").toString().trim()));
            	JobPojo job = JobDAO.getJobById(jobId);
            	MailUtil.sendApplicationStatusUpdate(user.getName(),user.getEmail(),job.getTitle(),job.getCompany(),newStatus);

                res.sendRedirect("ViewApplicantsServlet?jobId=" + jobId + "&status=" + newStatus);
            } else {
                res.sendRedirect("ViewApplicantsServlet?jobId=" + jobId + "&error=update_failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("error.jsp");
        }
    }
}