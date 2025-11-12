// Hello teammates who are reading this, if you are reading this just to figure out
// how to interact with my monstrosity, you can safely ignore every comment, except for
// the ones that start with a '!'. Those actually contain important info, everything else
// is just so I remember what the code does when/if I come back to it later

import {Room} from './room.js';
export {Room};

// this is just the standard lines for recieving a request
export default {
  async fetch(request, env) {

    // making the url a URL obejct so that i can invoke things like .pathname without
    // needing to manually sort through it
    const url = new URL(request.url);

    // ! the call here should be "shrill-forest-40bb.sw-william08.workers.dev/api/newRoom/<pin>"
    // ! and it should make the DO, save it to a PIN, and just push to the DO's 'actual' code
    // ! no clue how it will handle empty pins, so make sure there is actually a pin there
    if (request.method === 'POST' && url.pathname.startsWith('/api/newRoom/')){
        // the amount of time i wasted before remembering that .slice works on strings is insane
        const pin = url.pathname.slice('/api/newRoom/'.length);
        const id = env.ROOMS_DO.idFromName(pin);
        const stub = env.ROOMS_DO.get(id);

        // prolly shouldn't store a DO object in the kv, so I converted it to a string
        await env.PIN_INDEX.put(pin, id.toString());
        return stub.fetch(request);
    }

    // ! the call here should be "shrill-forest-40bb.sw-william08.workers.dev/api/joinRoom/<pin>"
    // ! it should just grab the already created DO for the PIN, and then also push to the DO's 'actual' code
    // ! no clue how it will handle empty pins, so make sure there is actually a pin there
    if (request.method === 'GET' && url.pathname.startsWith('/api/joinRoom/')){
        const pin = url.pathname.slice('/api/joinRoom/'.length);
        const idString = await env.PIN_INDEX.get(pin);
        if (idString === null){
        // dunno if status changes anything here, but i read an article that its rly important, so i added it
        return new Response('room not found', {status:404});
        }
        // there should be a way to do this in 1 line, but im converting string to id to stub
        const id = env.ROOMS_DO.idFromString(idString);
        const stub = env.ROOMS_DO.get(id);
        return stub.fetch(request);
    }

    return new Response(null);

  }
};
