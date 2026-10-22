(() => {
 if(!GigsUp.requireRole('STUDENT'))return;const $=id=>document.getElementById(id),msg=$('dashMessage');
 function render(target,data){
  const el=$(target);
  if(!Array.isArray(data)||!data.length){
   el.innerHTML='<div class="empty-state">No shifts returned for this section.</div>';
   return;
  }
  el.innerHTML=data.map(s=>`<article class="shift-item">
   <div>
    <h3>${GigsUp.escape(s.title||'Untitled shift')}</h3>
    <p>${GigsUp.escape(s.description||'No description provided')}</p>
    <p>Vendor: ${GigsUp.escape(s.vendorName||'Business')} · ${GigsUp.escape(GigsUp.date(s.startTime))} – ${GigsUp.escape(GigsUp.date(s.endTime))}</p>
   </div>
   <div class="shift-meta">
    <span class="shift-pay">${GigsUp.escape(GigsUp.money(s.payRate))}</span>
    <span class="shift-status">${GigsUp.escape(s.status||'UNKNOWN')}</span>
    ${target==='matchResults' && s.status==='OPEN'
      ? `<button class="btn btn-primary accept-shift" data-shift-id="${Number(s.id)}">Accept Shift</button>`
      : ''}
    ${target==='acceptedResults' && s.status==='MATCHED'
      ? `<button class="btn btn-primary complete-shift" data-shift-id="${Number(s.id)}">Complete Shift</button>`
      : ''}
   </div>
  </article>`).join('');
 }
 async function refresh(){GigsUp.clearMessage(msg);try{const [accepted,availability]=await Promise.all([GigsUp.api('/api/shifts/accepted'),GigsUp.api('/api/availability')]);$('acceptedCount').textContent=Array.isArray(accepted)?accepted.length:'—';$('availabilityCount').textContent=Array.isArray(availability)?availability.length:'—';render('acceptedResults',accepted);$('availabilityResults').innerHTML=Array.isArray(availability)&&availability.length?availability.map(a=>`<article class="shift-item"><div><h3>Availability window</h3><p>${GigsUp.escape(GigsUp.date(a.availableFrom))} — ${GigsUp.escape(GigsUp.date(a.availableTo))}</p></div></article>`).join(''):'<div class="empty-state">No availability windows saved yet.</div>';}catch(e){GigsUp.showMessage(msg,e.message);}}
 $('matchResults').addEventListener('click',async e=>{
  const button=e.target.closest('.accept-shift');
  if(!button)return;
  const id=Number(button.dataset.shiftId);
  if(!Number.isInteger(id)||id<=0){
   GigsUp.showMessage(msg,'Invalid shift ID.');
   return;
  }
  if(!confirm('Accept this shift?'))return;
  button.disabled=true;
  button.textContent='Accepting...';
  GigsUp.clearMessage(msg);
  try{
   await GigsUp.api(`/api/shifts/${id}/accept`,{method:'POST'});
   GigsUp.showMessage(msg,'Shift accepted successfully!','success');
   await refresh();
   const lat=Number($('matchLat').value);
   const lng=Number($('matchLng').value);
   const radiusMeters=Number($('matchRadius').value);
   const shifts=await GigsUp.api('/api/shifts/match',{
    method:'POST',
    body:JSON.stringify({lat,lng,radiusMeters})
   });
   render('matchResults',shifts);
  }catch(err){
   GigsUp.showMessage(msg,err.message);
   button.disabled=false;
   button.textContent='Accept Shift';
  }
 });

 $('acceptedResults').addEventListener('click',async e=>{
  const button=e.target.closest('.complete-shift');
  if(!button)return;
  const id=Number(button.dataset.shiftId);
  if(!Number.isInteger(id)||id<=0)return;
  if(!confirm('Mark this shift as completed?'))return;
  button.disabled=true;
  button.textContent='Completing...';
  GigsUp.clearMessage(msg);
  try{
   await GigsUp.api(`/api/shifts/${id}/complete`,{method:'POST'});
   GigsUp.showMessage(msg,'Shift completed successfully!','success');
   await refresh();
  }catch(err){
   GigsUp.showMessage(msg,err.message);
   button.disabled=false;
   button.textContent='Complete Shift';
  }
 });
 $('matchForm').addEventListener('submit',async e=>{
  e.preventDefault();
  GigsUp.clearMessage(msg);

  const lat=Number($('matchLat').value);
  const lng=Number($('matchLng').value);
  const radiusMeters=Number($('matchRadius').value);

  if(!Number.isFinite(lat)||lat < -90||lat > 90||
     !Number.isFinite(lng)||lng < -180||lng > 180||
     !Number.isFinite(radiusMeters)||radiusMeters < 1||radiusMeters > 100000){
    GigsUp.showMessage(msg,'Enter valid coordinates and a radius between 1 and 100000 meters.');
    return;
  }

  try{
    const shifts=await GigsUp.api('/api/shifts/match',{
      method:'POST',
      body:JSON.stringify({lat,lng,radiusMeters})
    });
    render('matchResults',shifts);
    GigsUp.showMessage(msg,'Shift search completed.','success');
  }catch(err){
    GigsUp.showMessage(msg,err.message);
  }
 });
 $('availabilityForm').addEventListener('submit',async e=>{e.preventDefault();GigsUp.clearMessage(msg);const from=new Date($('availableFrom').value),to=new Date($('availableTo').value);if(!(from>new Date())||!(to>from)){GigsUp.showMessage(msg,'Choose a future start time and an end time after the start.');return;}try{await GigsUp.api('/api/availability',{method:'POST',body:JSON.stringify({availableFrom:from.toISOString(),availableTo:to.toISOString()})});GigsUp.showMessage(msg,'Availability saved.','success');await refresh();}catch(err){GigsUp.showMessage(msg,err.message);}});$('refreshStudent').addEventListener('click',refresh);refresh();
})();
