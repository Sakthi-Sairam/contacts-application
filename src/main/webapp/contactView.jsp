<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.models.Contact, com.models.ContactPhoneNumber, com.models.ContactEmail, java.util.List" %>

<%
    Contact contact = (Contact) request.getAttribute("contact");
    List<ContactPhoneNumber> phoneNumbers = contact.getPhoneNumbers();
    List<ContactEmail> emails = contact.getEmails();
    
    // Get first letter of alias name for avatar
    String avatarInitial = contact.getAlias_name() != null && !contact.getAlias_name().isEmpty() ? 
        contact.getAlias_name().substring(0, 1).toUpperCase() : "?";
    
    // Format timestamps if available
    String createdDate = contact.getCreatedAt() > 0 ? 
        new java.text.SimpleDateFormat("MMM dd, yyyy").format(new java.util.Date(contact.getCreatedAt())) : "N/A";
    String modifiedDate = contact.getModifiedAt() > 0 ? 
        new java.text.SimpleDateFormat("MMM dd, yyyy").format(new java.util.Date(contact.getModifiedAt())) : "N/A";
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Contact Details - <%= contact.getAlias_name() %></title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/dashboard.css">
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/contact-details.css">
</head>
<body>
	<div class="app-container">
		<input type="checkbox" id="sidebar-toggle" class="sidebar-toggle">
		<label for="sidebar-toggle" class="sidebar-toggle-label">
			<i class="bi bi-list"></i>
		</label>
		<div class="sidebar">
			<div class="sidebar-header">
				<i class="bi bi-person-rolodex sidebar-logo"></i>
				<h2>Contact Manager</h2>
			</div>
			<nav class="sidebar-nav">
				<a class="active" href="/contacts"> <i
					class="bi bi-person-lines-fill"></i> <span>All Contacts</span>
				</a> <a href="/profile"> <i class="bi bi-person-circle"></i> <span>Profile</span>
				</a> <a href="/group/archived"> <i class="bi bi-archive"></i> <span>Archived</span>
				</a> <a href="/group/favourites"> <i class="bi bi-heart"></i> <span>Favourites</span>
				</a> <a href="/merge-contacts"> <i class="bi bi-intersect me-2"></i><span>Merge Duplicates</span>
				</a> <a href="/categories"> <i class="bi bi-tags"></i> <span>Categories</span>
				</a> <a href="logout" class="logout"> <i
					class="bi bi-box-arrow-right"></i> <span>Logout</span>
				</a>
			</nav>
		</div>
<div class="content">
			<%
			String errorMessage = (String) request.getParameter("errorMessage");
			if (errorMessage != null && !errorMessage.isEmpty()) {
			%>
			<div class="alert alert-danger alert-dismissible fade show"
				role="alert">
				<i class="bi bi-exclamation-triangle-fill me-2"></i> <strong>Error:</strong>
				<%=errorMessage%>
				<button type="button" class="btn-close" data-bs-dismiss="alert"
					aria-label="Close"></button>
			</div>
			<%
			}
			%>
    <div class="container mt-4">
        <div class="contact-details-container">
            <div class="contact-header">
                <div class="contact-avatar">
                    <%= avatarInitial %>
                </div>
                <div class="contact-title">
                    <h1><%= contact.getAlias_name() %></h1>
                    <div class="contact-meta">
                        <% if(contact.getResourceName() != null && !contact.getResourceName().isEmpty()) { %>
                            <div><small class="text-muted"><i class="bi bi-link-45deg"></i> <%= contact.getResourceName() %></small></div>
                        <% } %>
                        <div><small class="text-muted"><i class="bi bi-calendar2-check"></i> Created: <%= createdDate %></small></div>
                        <div><small class="text-muted"><i class="bi bi-calendar2-plus"></i> Modified: <%= modifiedDate %></small></div>
                        <div class="mt-2">
                            <% if(contact.getIsFavorite() == 1) { %>
                                <span class="status-badge status-favorite"><i class="bi bi-star-fill"></i> Favorite</span>
                            <% } %>
                            <% if(contact.getIsArchived() == 1) { %>
                                <span class="status-badge status-archived"><i class="bi bi-archive-fill"></i> Archived</span>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="contact-actions">
                <button type="button" class="btn btn-contact-primary" data-bs-toggle="modal" data-bs-target="#editContactModal">
                    <i class="bi bi-pencil-square"></i> Edit Contact
                </button>
                <form action="/contacts/<%=contact.getMyContactsID()%>?action=delete" method="post" style="display: inline;">
                    <button type="submit" class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this contact?');">
                        <i class="bi bi-trash"></i> Delete
                    </button>
                </form>
                <a href="/contacts" class="btn btn-secondary">
                    <i class="bi bi-arrow-left"></i> Back to Contacts
                </a>
            </div>
            
            <div class="row">
                <div class="col-md-12">
                    <table class="table contact-table">
                        <tbody>
                            <tr>
                                <th><i class="bi bi-envelope-fill me-2"></i> Emails</th>
                                <td>
                                    <div class="contact-details">
                                        <% if(emails == null || emails.isEmpty()) { %>
                                            <div class="text-muted fst-italic">No emails added yet</div>
                                        <% } else { %>
                                            <% for (ContactEmail email : emails) { %>
                                                <div class="contact-detail-item">
                                                    <% if(email.getLabel() != null && !email.getLabel().equalsIgnoreCase("null") && !email.getLabel().isBlank()) { %>
                                                        <span class="contact-detail-label"><%= email.getLabel() %></span>
                                                    <% } else { %>
                                                        <span class="contact-detail-label">Email</span>
                                                    <% } %>
                                                    <span class="contact-detail-value"><%= email.getEmail() %></span>
                                                    <div class="contact-detail-actions">
                                                        <form action="/contacts/<%=email.getContactId()%>/<%=email.getContactEmailId()%>?action=deleteEmail" method="post">
                                                        <button type="submit" class="btn-icon text-danger" title="Delete email" style="border:none">
                                                            <i class="bi bi-trash-fill"></i>
                                                        </button>
                                                    </form>
                                                    </div>
                                                </div>
                                            <% } %>
                                        <% } %>
                                        <button type="button" class="btn btn-contact-secondary mt-2" data-bs-toggle="modal" data-bs-target="#addEmailModal">
                                            <i class="bi bi-plus-circle"></i> Add Email
                                        </button>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <th><i class="bi bi-telephone-fill me-2"></i> Phone Numbers</th>
                                <td>
                                    <div class="contact-details">
                                        <% if(phoneNumbers == null || phoneNumbers.isEmpty()) { %>
                                            <div class="text-muted fst-italic">No phone numbers added yet</div>
                                        <% } else { %>
                                            <% for (ContactPhoneNumber phone : phoneNumbers) { %>
                                                <div class="contact-detail-item">
                                                    <% if(phone.getLabel() != null && !phone.getLabel().equalsIgnoreCase("null") && !phone.getLabel().isBlank()) { %>
                                                        <span class="contact-detail-label"><%= phone.getLabel() %></span>
                                                    <% } else { %>
                                                        <span class="contact-detail-label">Phone</span>
                                                    <% } %>
                                                    <span class="contact-detail-value"><%= phone.getPhoneNumber() %></span>
                                                    <div class="contact-detail-actions">
                                                    <form action="/contacts/<%=phone.getContactId()%>/<%=phone.getContactNumberId()%>?action=deletePhone" method="post">
                                                        <button type="submit" class="btn-icon text-danger" title="Delete phone" style="border:none">
                                                            <i class="bi bi-trash-fill"></i>
                                                        </button>
                                                    </form>
                                                    </div>
                                                </div>
                                            <% } %>
                                        <% } %>
                                        <button type="button" class="btn btn-contact-secondary mt-2" data-bs-toggle="modal" data-bs-target="#addPhoneNumberModal">
                                            <i class="bi bi-plus-circle"></i> Add Phone Number
                                        </button>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <th><i class="bi bi-geo-alt-fill me-2"></i> Address</th>
                                <td>
                                    <% if(contact.getAddress() != null && !contact.getAddress().isBlank() && !contact.getAddress().equalsIgnoreCase("null")) { %>
                                        <%= contact.getAddress() %>
                                    <% } else { %>
                                        <span class="text-muted fst-italic">No address added yet</span>
                                    <% } %>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Edit Contact Modal -->
        <div class="modal fade" id="editContactModal" tabindex="-1" aria-labelledby="editContactModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editContactModalLabel">Edit Contact</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="/contacts/<%=contact.getMyContactsID()%>?action=edit" method="post">
                            <div class="form-floating mb-3">
                                <input type="text" id="alias_fnd_name" name="alias_fnd_name" value="<%=contact.getAlias_name()%>" required class="form-control">
                                <label for="alias_fnd_name">Alias</label>
                            </div>

                            <div class="form-floating mb-3">
                                <input type="text" id="address" name="address" value="<%=contact.getAddress()%>" class="form-control">
                                <label for="address">Address</label>
                            </div>
                            
                            <div class="row mb-3">
                                <div class="col">
                                    <label class="form-label">Contact Status:</label>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" id="isFavorite" name="isFavorite" value="1" <%=contact.getIsFavorite() == 1 ? "checked" : ""%>>
                                        <label class="form-check-label" for="isFavorite">
                                            <i class="bi bi-star-fill text-warning"></i> Mark as Favorite
                                        </label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" id="isArchived" name="isArchived" value="1" <%=contact.getIsArchived() == 1 ? "checked" : ""%>>
                                        <label class="form-check-label" for="isArchived">
                                            <i class="bi bi-archive-fill text-secondary"></i> Archive Contact
                                        </label>
                                    </div>
                                </div>
                            </div>
                            
                            <input type="hidden" name="isFavorite" value="0">
                            <input type="hidden" name="isArchived" value="0">
                            
                            <div class="text-end">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <input type="submit" class="btn btn-contact-primary" value="Update Contact">
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add Phone Number Modal -->
        <div class="modal fade" id="addPhoneNumberModal" tabindex="-1" aria-labelledby="addPhoneNumberModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title"><i class="bi bi-telephone-plus-fill me-2"></i>Add Phone Number</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="/contacts/<%=contact.getMyContactsID()%>?action=addPhone" method="post">
                            <div class="form-floating mb-3">
                                <input type="text" id="phoneLabel" name="label" class="form-control">
                                <label for="phoneLabel">Label (e.g., Mobile, Home, Work)</label>
                            </div>
                            <div class="form-floating mb-3">
                                <input type="tel" id="phoneNumber" name="phoneNumber" required class="form-control" pattern="[0-9\+\-\.\(\) ]+">
                                <label for="phoneNumber">Phone Number</label>
                                <div class="form-text">Enter a valid phone number format</div>
                            </div>
                            <div class="text-end">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <input type="submit" class="btn btn-contact-primary" value="Add Phone Number">
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add Email Modal -->
        <div class="modal fade" id="addEmailModal" tabindex="-1" aria-labelledby="addEmailModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title"><i class="bi bi-envelope-plus-fill me-2"></i>Add Email</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="/contacts/<%=contact.getMyContactsID()%>?action=addEmail" method="post">
                            <div class="form-floating mb-3">
                                <input type="text" id="emailLabel" name="label" class="form-control">
                                <label for="emailLabel">Label (e.g., Personal, Work)</label>
                            </div>
                            <div class="form-floating mb-3">
                                <input type="email" id="email" name="email" required class="form-control">
                                <label for="email">Email Address</label>
                            </div>
                            <div class="text-end">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <input type="submit" class="btn btn-contact-primary" value="Add Email">
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>

<script>
// Fix for checkbox handling in form submission
document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('#editContactModal form');
    form.addEventListener('submit', function() {
        if (!document.getElementById('isFavorite').checked) {
            document.getElementsByName('isFavorite')[1].value = "0";
        } else {
            document.getElementsByName('isFavorite')[1].disabled = true;
        }
        
        if (!document.getElementById('isArchived').checked) {
            document.getElementsByName('isArchived')[1].value = "0";
        } else {
            document.getElementsByName('isArchived')[1].disabled = true;
        }
    });
});
</script>

</body>
</html>