function showModal(title, content, options = {}) {
  const {
    size = 'medium',
    onClose = null,
    showCloseButton = true,
    closeOnBackdrop = true
  } = options;

  const sizeClasses = {
    small: 'max-width: 400px;',
    medium: 'max-width: 600px;',
    large: 'max-width: 900px;',
    full: 'max-width: 95%; width: 1200px;'
  };

  const modalId = 'modal-' + Date.now();

  const backdrop = document.createElement('div');
  backdrop.className = 'modal-backdrop';
  backdrop.id = modalId;

  backdrop.innerHTML = `
    <div class="modal-content" style="${sizeClasses[size]} width: 100%; margin: 20px;" onclick="event.stopPropagation()">
      <div class="card" style="padding: 0; max-height: 90vh; display: flex; flex-direction: column;">
        <div style="padding: 24px; border-bottom: 1px solid var(--gray-200); display: flex; align-items: center; justify-content: space-between;">
          <h3 style="font-size: 1.5rem; font-weight: 700;">${title}</h3>
          ${showCloseButton ? `
            <button onclick="closeModal('${modalId}')" style="background: none; border: none; font-size: 24px; color: var(--gray-500); cursor: pointer; padding: 4px;">
              ✕
            </button>
          ` : ''}
        </div>

        <div class="modal-body" style="padding: 24px; overflow-y: auto; flex: 1;">
          ${content}
        </div>
      </div>
    </div>
  `;

  document.body.appendChild(backdrop);

  if (closeOnBackdrop) {
    backdrop.addEventListener('click', (e) => {
      if (e.target === backdrop) {
        closeModal(modalId);
        if (onClose) onClose();
      }
    });
  }

  document.addEventListener('keydown', function escapeHandler(e) {
    if (e.key === 'Escape') {
      closeModal(modalId);
      if (onClose) onClose();
    }
  });

  return modalId;
}

function closeModal(modalId) {
  const modal = document.getElementById(modalId);

  if (modal) {
    modal.style.animation = 'fadeOut 0.2s ease';
    setTimeout(() => {
      modal.remove();
    }, 200);
  }
}

function showFormModal(title, fields, onSubmit) {
  const formId = 'form-' + Date.now();

  const formFields = fields.map((field, index) => {
    const fieldId = `${formId}-field-${index}`;

    switch (field.type) {
      case 'text':
      case 'email':
      case 'number':
      case 'date':
      case 'password':
        return `
          <div class="form-group">
            <label class="form-label">${field.label}</label>
            <input type="${field.type}" id="${fieldId}" name="${field.name}" class="form-input" value="${field.value || ''}">
          </div>
        `;

      case 'textarea':
        return `
          <div class="form-group">
            <label class="form-label">${field.label}</label>
            <textarea id="${fieldId}" name="${field.name}" class="form-input">${field.value || ''}</textarea>
          </div>
        `;

      case 'select':
        return `
        <div class="form-group">
          <label class="form-label">${field.label}</label>
          <select 
            id="${fieldId}" 
            name="${field.name}" 
            class="form-select"
          >
            ${(field.options || []).map(option => `
              <option value="${option.value}">
                ${option.label}
              </option>
            `).join('')}
          </select>
        </div>
      `;

      default:
        return '';
    }
  }).join('');

  const content = `
    <form id="${formId}">
      ${formFields}
      <div style="display:flex; gap:12px; justify-content:flex-end; margin-top:24px;">
        <button type="button" class="btn btn-outline">Cancel</button>
        <button type="submit" class="btn btn-primary">Submit</button>
      </div>
    </form>
  `;

  const modalId = showModal(title, content);

  document.getElementById(formId)
    .querySelector('.btn-outline')
    .onclick = () => closeModal(modalId);

  document.getElementById(formId).addEventListener('submit', async (e) => {
    e.preventDefault();

    const formData = new FormData(e.target);
    const data = Object.fromEntries(formData.entries());

    await onSubmit(data);
    closeModal(modalId);
  });
}

function showDetailsModal(title, data) {
  const content = `
    <div style="display:grid; grid-template-columns:repeat(auto-fit,minmax(200px,1fr)); gap:20px;">
      ${Object.entries(data).map(([key, value]) => `
        <div>
          <div style="font-size:12px; font-weight:600; text-transform:uppercase;">
            ${key}
          </div>
          <div style="font-size:14px;">
            ${value || '-'}
          </div>
        </div>
      `).join('')}
    </div>
  `;

  showModal(title, content, { size: 'large' });
}