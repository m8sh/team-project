export class Room{
  constructor(state, env){
  this.state = state;
  this.env = env;
  this.questions = [];
  this.hostSocket = null;
  this.players = new Map();
  }

  async fetch(request) {
    if (request.headers.get('Upgrade') === 'websocket'){
    return this.handleWebSocket(request);
    }
    const url = new URL(request.url);
    if (request.method === 'POST' && url.pathname.startsWith('/api/newRoom/')){
    return new Response('new room created');
    }
    if (request.method === 'GET' && url.pathname.startsWith('/api/joinRoom/')){
    return new Response('room joined');
    }
  return new Response('Room DO works');
  }

  function handleWebSocket(request){
    const webSocketPair = new WebSocketPair();
    const client = webSocketPair[0];
    const server = webSocketPair[1];
    server.accept();

    server.addEventListener('message', messageAccept(request, server));
    server.addEventListener('close', closeRoom(server));

    return new Response(null,{webSocket: client})
  }

  function messageAccept(request, server){
    let msg;
    try {
        msg = JSON.parse(request.data);
    }
    catch{
        return;
    }
    // marks the person sending this message as the host for that pin
    if (msg.type === 'host/setHost'){
        if(this.hostSocket === null){
        this.hostSocket = server;
        }
        return;
    }

    // lets the person join
    if (msg.type === 'client/join'){
        const name = (msg.username).toString();
        players.set(name, client);
        hostSocket.send(name + ' joined');
    }

    if (msg.type === 'host/endGame'){
        this.closerRoom(server);
    }

    if (msg.type === 'host/sendQuestions'){

    }

    if (msg.type === 'host/receiveAnswers'){
        hostSocket.send(msg)
    }

    if (msg.type === 'client/receiveQuestions'){

    }

    if (msg.type === 'client/sendAnswers'){

    }

  }

  function closeRoom(server){

  }

}
