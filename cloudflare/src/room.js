export class Room{
  constructor(state, env){
  this.state = state;
  this.env = env;}

  async fetch(request) {
  return new Response('Room DO alive');}
}
